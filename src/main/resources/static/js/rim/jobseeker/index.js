// 구직자 페이지 관련 기능을 window 객체에 namespace로 관리
window.jobseeker = {
    // DOM 요소
    elements: {
        dashboardContainer: document.getElementById('dashboardContainer'),
        recommendedPosts: document.getElementById('recommendedPosts'),
        deadlinePosts: document.getElementById('deadlinePosts'),
        companyFilterButtons: document.querySelectorAll('.top-companies .filter-buttons button')
    },

    // 페이지 초기화
    async init() {
        try {
            // API.main.getData() 사용
            const response = await API.main.getData();
            console.log('메인 데이터:', response);
            
            // 구직자 섹션 데이터 추출
            const jobSeekerData = response.userSection?.jobSeeker;
            if (!jobSeekerData) {
                console.error('구직자 데이터가 없습니다');
                return;
            }
            
            // 각 섹션 렌더링
            this.renderDashboard(jobSeekerData.dashboard);
            this.renderRecommendedPosts(jobSeekerData.recommendedPosts);
            this.renderDeadlinePosts(jobSeekerData.deadlinePosts);
            
            // 공통 기능 사용 (TopCompanies 렌더링)
            if (jobSeekerData.topCompanies) {
                window.common.guestSection.renderTopCompanies(jobSeekerData.topCompanies);
            }
            
            // 이벤트 리스너 등록
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
                                    <div class="apply-date">${new Date(app.applyDate).toLocaleDateString()}</div>
                                </div>
                                <span class="application-status status-${getStatusClass(app.passYn)}">
                                    ${getStatusText(app.passYn)}
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
