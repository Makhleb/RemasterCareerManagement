// 구직자 페이지 관련 기능을 window 객체에 namespace로 관리
window.jobseeker = {
    // 차트 인스턴스 저장
    chart: null,

    // DOM 요소
    elements: {
        dashboardContainer: document.getElementById('dashboardContainer'),
        recommendedPosts: document.getElementById('recommendedPosts'),
        deadlinePosts: document.getElementById('deadlinePosts'),
        companyFilterButtons: document.querySelectorAll('.top-companies .filter-buttons button')
    },

    // 유틸리티 함수들
    utils: {
        getStatusClass(status) {
            switch (status) {
                case 'W': return 'status-waiting';  // 대기중
                case 'Y': return 'status-pass';     // 합격
                case 'N': return 'status-fail';     // 불합격
                default: return 'status-default';   // 기본
            }
        },

        getStatusText(status) {
            switch (status) {
                case 'W': return '대기중';
                case 'Y': return '합격';
                case 'N': return '불합격';
                default: return '확인중';
            }
        },

        formatDate(dateString) {
            const date = new Date(dateString);
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
        }
    },

    // 페이지 초기화
    init(response) {
        const jobSeekerData = response.userSection?.jobSeeker;
        if (!jobSeekerData) return;

        // 대시보드 렌더링
        this.renderDashboard(jobSeekerData.dashboard);
        
        // 맞춤 추천 공고 렌더링
        this.renderRecommendedPosts(jobSeekerData.recommendedPosts);
        
        // 마감 임박 공고 렌더링
        this.renderDeadlinePosts(jobSeekerData.deadlinePosts);
        
        // 인기 채용공고 렌더링
        this.renderPopularPosts(jobSeekerData.popularPosts);
        this.renderPopularSkills(response.popularSkills);
        
        // TOP 10 기업 렌더링
        if (jobSeekerData.topCompanies) {
            this.companies = jobSeekerData.topCompanies;
            this.renderTopCompanies(this.companies);
            this.initCompanyFilterButtons();
        }
        
        // 주목받는 채용공고 렌더링
        this.renderTrendingPosts(jobSeekerData.scrapedPosts);
    },

    // 대시보드 렌더링
    renderDashboard(dashboard) {
        const container = document.getElementById('dashboardContainer');
        if (!container) return;

        // 데이터가 없는 경우
        if (!dashboard || !dashboard.stats || !dashboard.recentApplications || 
            dashboard.recentApplications.length === 0) {
            container.innerHTML = `
                <div class="empty-state dashboard-prompt">
                    <i class="fas fa-clipboard-list"></i>
                    <p>아직 지원 내역이 없습니다.<br>새로운 채용공고를 확인해보세요!</p>
                    <a href="/view/users/job-post/list" class="jobs-link">
                        <button class="jobs-button">
                            채용공고 보러가기
                        </button>
                    </a>
                </div>
            `;
            return;
        }

        // 데이터가 있는 경우 기존 렌더링
        container.innerHTML = `
            <!-- 지원 현황 차트 -->
            <div class="stats-chart">
                <h3>지원 현황</h3>
                <div class="chart-container">
                    <canvas id="applicationChart"></canvas>
                </div>
                <div class="stats-summary">
                    <div class="stat-item">
                        <span class="stat-value" id="inProgressCount">0</span>
                        <span class="stat-label">진행중</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-value" id="acceptedCount">0</span>
                        <span class="stat-label">합격</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-value" id="rejectedCount">0</span>
                        <span class="stat-label">불합격</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-value" id="totalCount">0</span>
                        <span class="stat-label">총 지원</span>
                    </div>
                </div>
            </div>
            
            <!-- 최근 지원 내역 -->
            <div class="recent-applications">
                <div class="recent-header">
                    <div class="title-area">
                        <h3>최근 지원 내역</h3>
                        <span class="period-info">최근 30일</span>
                    </div>
                    <div class="status-summary">
                        <span class="status-count accepted">
                            합격 <strong id="recentAcceptedCount">0</strong>
                        </span>
                        <span class="status-count waiting">
                            진행중 <strong id="recentWaitingCount">0</strong>
                        </span>
                        <span class="status-count rejected">
                            불합격 <strong id="recentRejectedCount">0</strong>
                        </span>
                    </div>
                </div>
                <div class="applications-list" id="recentApplicationsList">
                    <!-- JavaScript로 동적 렌더링 -->
                </div>
            </div>
        `;

        // 데이터 렌더링
        this.updateStats(dashboard.stats);
        this.renderApplicationChart(dashboard.stats);
        this.renderRecentApplications(dashboard.recentApplications);
    },

    // 통계 수치 업데이트
    updateStats(stats) {
        document.getElementById('inProgressCount').textContent = stats?.inProgress || 0;
        document.getElementById('acceptedCount').textContent = stats?.accepted || 0;
        document.getElementById('rejectedCount').textContent = stats?.rejected || 0;
        document.getElementById('totalCount').textContent = stats?.total || 0;
    },

    // 지원 현황 차트 렌더링
    renderApplicationChart(stats) {
        const ctx = document.getElementById('applicationChart').getContext('2d');
        
        // 기존 차트 제거
        if (this.chart) {
            this.chart.destroy();
        }

        // 데이터가 없거나 모든 값이 0인 경우 처리
        const total = (stats?.inProgress || 0) + (stats?.accepted || 0) + (stats?.rejected || 0);
        
        if (total === 0) {
            // 데이터가 없는 경우 기본 차트 표시
            this.renderEmptyChart(ctx);
            return;
        }

        const calculatePercentage = (value) => ((value / total) * 100).toFixed(1);

        this.chart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: [
                    `진행중 ${calculatePercentage(stats.inProgress || 0)}%`,
                    `합격 ${calculatePercentage(stats.accepted || 0)}%`,
                    `불합격 ${calculatePercentage(stats.rejected || 0)}%`
                ],
                datasets: [{
                    data: [
                        stats.inProgress || 0,
                        stats.accepted || 0,
                        stats.rejected || 0
                    ],
                    backgroundColor: [
                        '#B9B0B2',  // primary-color-3
                        '#b76737',  // primary-color-1
                        '#3A5154'   // primary-color-2
                    ],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            font: {
                                family: 'KopupDodum',
                                size: 12
                            },
                            padding: 15,
                            usePointStyle: true,
                            pointStyle: 'circle'
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.raw || 0;
                                return `${label} (${value}건)`;
                            }
                        }
                    }
                }
            }
        });
    },

    // 데이터가 없는 경우의 차트 렌더링
    renderEmptyChart(ctx) {
        this.chart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['지원 내역이 없습니다'],
                datasets: [{
                    data: [1],
                    backgroundColor: ['#f5f5f5'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            font: {
                                family: 'KopupDodum',
                                size: 12
                            },
                            padding: 15,
                            usePointStyle: true,
                            pointStyle: 'circle'
                        }
                    },
                    tooltip: {
                        enabled: false
                    }
                }
            }
        });
    },

    // 최근 지원 내역 렌더링
    renderRecentApplications(applications) {
        const container = document.getElementById('recentApplicationsList');
        if (!container) return;

        if (!applications || applications.length === 0) {
            container.innerHTML = this.renderApplicationPrompt();
            // 상태 카운트도 0으로 표시
            document.getElementById('recentAcceptedCount').textContent = '0';
            document.getElementById('recentWaitingCount').textContent = '0';
            document.getElementById('recentRejectedCount').textContent = '0';
            return;
        }

        // 상태별 카운트 계산
        const counts = applications.reduce((acc, app) => {
            switch(app.passYn) {
                case 'Y': acc.accepted++; break;
                case 'N': acc.rejected++; break;
                case 'W': acc.waiting++; break;
            }
            return acc;
        }, { accepted: 0, waiting: 0, rejected: 0 });

        // 카운트 업데이트
        document.getElementById('recentAcceptedCount').textContent = counts.accepted;
        document.getElementById('recentWaitingCount').textContent = counts.waiting;
        document.getElementById('recentRejectedCount').textContent = counts.rejected;

        // 리스트 렌더링
        const html = applications.map(app => `
            <div class="application-item ${this.getStatusClass(app.passYn)}">
                <div class="company-info">
                    <span class="company-name">${app.companyName}</span>
                    <span class="post-title">${app.postTitle}</span>
                </div>
                <div class="application-info">
                    <span class="apply-date">${this.formatDate(app.applyDate)}</span>
                    <span class="status">${this.getStatusText(app.passYn)}</span>
                </div>
            </div>
        `).join('');

        container.innerHTML = html;
    },

    // 지원 내역이 없을 때 표시할 메시지와 버튼
    renderApplicationPrompt() {
        return `
            <div class="empty-state application-prompt">
                <i class="fas fa-clipboard-list"></i>
                <p>아직 지원 내역이 없습니다.<br>새로운 채용공고를 확인해보세요!</p>
                <a href="/view/users/job-post/list" class="jobs-link">
                    <button class="jobs-button">
                        채용공고 보러가기
                    </button>
                </a>
            </div>
        `;
    },

    // 상태에 따른 클래스 반환
    getStatusClass(status) {
        switch (status) {
            case 'W': return 'status-waiting';
            case 'Y': return 'status-accepted';
            case 'N': return 'status-rejected';
            default: return '';
        }
    },

    // 상태 텍스트 반환
    getStatusText(status) {
        switch (status) {
            case 'W': return '진행중';
            case 'Y': return '합격';
            case 'N': return '불합격';
            default: return '알 수 없음';
        }
    },

    // 날짜 포맷팅
    formatDate(dateString) {
        const date = new Date(dateString);
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${month}/${day}`;
    },

    // 추천 공고 렌더링
    renderRecommendedPosts(posts) {
        const container = document.getElementById('recommendedPosts');
        if (!container) return;
        
        if (!posts || posts.length === 0) {
            container.innerHTML = this.renderResumePrompt();
            return;
        }
        container.innerHTML = posts.map(window.common.guestSection.renderJobPostCard).join('');
    },

    // 이벤서 등록 유도 메시지 렌더링
    renderResumePrompt() {
        return `
            <div class="empty-state resume-prompt">
                <i class="fas fa-file-alt"></i>
                <p>이력서를 등록하면 맞춤 채용공고를 추천해드려요!</p>
                <a href="/resume/register" class="resume-link">
                    <button class="resume-button">
                        이력서 등록하기
                    </button>
                </a>
            </div>
        `;
    },

    // 마감 임박 공고 렌더링
    renderDeadlinePosts(posts) {
        const container = document.getElementById('deadlinePosts');
        if (!container) return;
        
        if (!posts || posts.length === 0) {
            container.innerHTML = this.renderEmptyState('마감 임박한 채용공고가 없습니다.');
            return;
        }
        container.innerHTML = posts.map(window.common.guestSection.renderJobPostCard).join('');
    },

    // 이벤트 리스너 초기화
    initEventListeners() {
        // 기업 필터 버튼 이벤트
        this.elements.companyFilterButtons.forEach(button => {
            button.addEventListener('click', this.handleCompanyFilter.bind(this));
        });
    },

    // 기업 필터 핸들러
    handleCompanyFilter(event) {
        const sortType = event.target.dataset.sort;
        // 필터링 로직...
    },

    // 기업 카드 렌더링
    renderCompanyCard(company) {
        return window.common.guestSection.renderCompanyCard(company);
    },

    // 기업 필터 버튼 초기화
    initCompanyFilterButtons() {
        const filterButtons = document.querySelectorAll('.filter-buttons button');
        filterButtons.forEach(button => {
            button.addEventListener('click', (e) => {
                // 활성 버튼 스타일 변경
                filterButtons.forEach(btn => btn.classList.remove('active'));
                e.target.classList.add('active');

                // 정렬 적용
                const sortType = e.target.dataset.sort;
                this.sortAndRenderCompanies(sortType);
            });
        });
    },

    // 기업 정렬 및 렌더링
    sortAndRenderCompanies(sortType) {
        if (!this.companies) return;

        const sortedCompanies = [...this.companies].sort((a, b) => {
            if (sortType === 'rating') {
                return b.avgRating - a.avgRating;
            } else if (sortType === 'interest') {
                return b.likeCount - a.likeCount;
            }
            return 0;
        });

        this.renderTopCompanies(sortedCompanies);
    },

    // TOP 10 기업 렌더링
    renderTopCompanies(companies) {
        const container = document.getElementById('topCompanies');
        if (!container) return;
        
        if (!companies || companies.length === 0) {
            container.innerHTML = this.renderEmptyState('등록된 기업 정보가 없습니다.');
            return;
        }
        container.innerHTML = companies.map(this.renderCompanyCard).join('');
        this.initializeSlider();
    },

    // 슬라이더 초기화
    initializeSlider() {
        window.common.guestSection.initializeSlider();
    },

    // 인기 채용공고 렌더링 메서드 추가
    renderPopularPosts(posts) {
        const container = document.getElementById('popularPosts');
        if (!container) return;
        
        if (!posts || posts.length === 0) {
            container.innerHTML = this.renderEmptyState('인기 채용공고가 없습니다.');
            return;
        }
        container.innerHTML = posts.map(window.common.guestSection.renderJobPostCard).join('');
    },

    // 주목받는 채용공고 렌더링 메서드 추가
    renderTrendingPosts(posts) {
        const container = document.getElementById('trendingPosts');
        if (!container) return;
        
        if (!posts || posts.length === 0) {
            container.innerHTML = this.renderEmptyState('주목받는 채용공고가 없습니다.');
            return;
        }
        container.innerHTML = posts.map(window.common.guestSection.renderJobPostCard).join('');
    },

    // 인기 스킬 렌더링
    renderPopularSkills(skills) {
        if (!skills) return;
        
        const container = document.querySelector('.skill-tags');
        if (!container) return;

        const skillTags = skills.slice(0, 3).map(skill => `
            <span class="skill-tag">
                🔥${skill.skillName}
                <span class="count">${skill.postCount}개의 구인공고!</span>
            </span>
        `).join('');

        container.innerHTML = skillTags;
    },

    // 공통 "데이터 없음" 메시지 렌더링 함수
    renderEmptyState(message) {
        return `
            <div class="empty-state">
                <i class="fas fa-info-circle"></i>
                <p>${message}</p>
            </div>
        `;
    }
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    // 공통 기능 초기화 (검색 등)
    window.common.search.init();
    // 구직자 페이지 초기화
    window.jobseeker.init();
});
