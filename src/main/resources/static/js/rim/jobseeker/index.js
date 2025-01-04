// 구직자 페이지 관련 기능을 window 객체에 namespace로 관리
window.jobseeker = {
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
    async init() {
        try {
            const response = await API.main.getData();
            console.log('메인 데이터:', response);
            
            const jobSeekerData = response.userSection?.jobSeeker;
            if (!jobSeekerData) {
                console.error('구직자 데이터가 없습니다');
                return;
            }
            
            this.renderDashboard(jobSeekerData.dashboard);
            this.renderRecommendedPosts(jobSeekerData.recommendedPosts);
            this.renderDeadlinePosts(jobSeekerData.deadlinePosts);
            
            if (jobSeekerData.topCompanies) {
                window.common.guestSection.renderTopCompanies(jobSeekerData.topCompanies);
            }
            
            this.initEventListeners();
            
        } catch (error) {
            console.error('구직자 메인 페이지 로딩 실패:', error);
        }
    },

    // 대시보드 렌더링
    renderDashboard(dashboardData) {
        if (!this.elements.dashboardContainer) return;
        
        const stats = dashboardData.stats;
        const total = stats.inProgress + stats.accepted + stats.rejected;
        const acceptanceRate = total > 0 ? ((stats.accepted / total) * 100).toFixed(1) : 0;

    const dashboardHtml = `
        <div class="section-header">
            <h2>지원 현황</h2>
            <div class="stats-summary">전체 지원: ${total}건 / 합격률: ${acceptanceRate}%</div>
        </div>
        <div class="dashboard-content">
            <div class="stats-container">
                <div class="stat-item">
                    <span class="stat-label">진행 중</span>
                    <span class="stat-value waiting">${stats.inProgress}</span>
                </div>
                <div class="stat-item">
                    <span class="stat-label">합격</span>
                    <span class="stat-value accepted">${stats.accepted}</span>
                </div>
                <div class="stat-item">
                    <span class="stat-label">불합격</span>
                    <span class="stat-value rejected">${stats.rejected}</span>
                </div>
            </div>

                <div class="recent-applications">
                    <h3>최근 지원 내역</h3>
                    <div class="applications-list">
                        ${dashboardData.recentApplications && dashboardData.recentApplications.length > 0
                            ? dashboardData.recentApplications.map(app => `
                                <div class="application-item">
                                    <div class="application-info">
                                        <div class="company-name">${app.companyName}</div>
                                        <div class="post-title">${app.postTitle}</div>
                                        <div class="apply-date">${this.utils.formatDate(app.applyDate)}</div>
                                    </div>
                                    <span class="application-status status-${this.utils.getStatusClass(app.passYn)}">
                                        ${this.utils.getStatusText(app.passYn)}
                                    </span>
                                </div>
                            `).join('')
                            : '<div class="no-data">최근 지원 내역이 없습니다.</div>'
                        }
                    </div>
                </div>
            </div>
        `;

        this.elements.dashboardContainer.innerHTML = dashboardHtml;
    },

    // 추천 공고 렌더링
    renderRecommendedPosts(posts) {
        if (!this.elements.recommendedPosts) return;
        this.elements.recommendedPosts.innerHTML = posts.map(window.common.guestSection.renderJobPostCard).join('');
    },

    // 마감 임박 공고 렌더링
    renderDeadlinePosts(posts) {
        if (!this.elements.deadlinePosts) return;
        this.elements.deadlinePosts.innerHTML = posts.map(window.common.guestSection.renderJobPostCard).join('');
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
    }
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    // 공통 기능 초기화 (검색 등)
    window.common.search.init();
    // 구직자 페이지 초기화
    window.jobseeker.init();
});
