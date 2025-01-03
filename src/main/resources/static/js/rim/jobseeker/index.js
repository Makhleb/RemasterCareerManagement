// DOM 요소
const DOM = {
    username: document.querySelector('.username'),
    dashboardContainer: document.getElementById('dashboardContainer'),
    recommendedPosts: document.getElementById('recommendedPosts'),
    topCompanies: document.getElementById('topCompanies'),
    popularPosts: document.getElementById('popularPosts'),
    deadlinePosts: document.getElementById('deadlinePosts'),
    companyFilterButtons: document.querySelectorAll('.top-companies .filter-buttons button'),
    prevButton: document.getElementById('slideLeft'),
    nextButton: document.getElementById('slideRight'),
    companiesSlider: document.getElementById('topCompanies')
};

// 페이지 초기화
async function initJobSeekerPage() {
    try {
        const response = await API.main.getJobSeekerData();
        console.log('구직자 메인 데이터:', response);
        
        // 사용자 이름 표시
        DOM.username.textContent = response.userName;
        
        // 각 섹션 렌더링
        renderDashboard(response.dashboard);
        renderRecommendedPosts(response.recommendedPosts);
        renderTopCompanies(response.topCompanies);
        renderPopularPosts(response.popularPosts);
        renderDeadlinePosts(response.deadlinePosts);
        
        // 슬라이더 초기화
        initializeSlider();
        
    } catch (error) {
        console.error('구직자 메인 페이지 로딩 실패:', error);
    }
}

// 각 섹션 렌더링 함수들
function renderDashboard(data) {
    if (!DOM.dashboardContainer) return;
}

function renderRecommendedPosts(posts) {
    if (!DOM.recommendedPosts) return;
    DOM.recommendedPosts.innerHTML = posts.map(JobPost.renderCard).join('');
}

function renderTopCompanies(companies) {
    if (!DOM.topCompanies) return;
    DOM.topCompanies.innerHTML = companies.map(CompanyCard.render).join('');
}

function renderPopularPosts(posts) {
    if (!DOM.popularPosts) return;
    DOM.popularPosts.innerHTML = posts.map(JobPost.renderCard).join('');
}

function renderDeadlinePosts(posts) {
    if (!DOM.deadlinePosts) return;
    DOM.deadlinePosts.innerHTML = posts.map(JobPost.renderCard).join('');
}

// 이벤트 리스너 등록
function initEventListeners() {
    // 기업 필터 버튼 이벤트
    DOM.companyFilterButtons.forEach(button => {
        button.addEventListener('click', handleCompanyFilter);
    });
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    initJobSeekerPage();
    initEventListeners();
}); 