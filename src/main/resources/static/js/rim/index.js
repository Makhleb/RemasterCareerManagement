document.addEventListener('DOMContentLoaded', async function() {
    // 비로그인 배너 표시/숨김 처리
    const user = await auth.getCurrentUser();
    const guestBanner = document.getElementById('guestBanner');
    if (user && user.type !== 'guest') {
        guestBanner.style.display = 'none';
    }

    // 인기 기술스택별 채용공고 로드
    try {
        const response = await API.main.getPopularPosts();
        console.log('index.js  ->   API.main.getPopularPosts() response', response);
        console.log('index.js  ->   API.main.getPopularPosts() response.data', response.data);
        const posts = response.data.data;
        renderPopularPosts(posts);
    } catch (error) {
        console.error('인기 채용공고 로드 실패:', error);
    }

    // TOP 10 기업 로드
    try {
        const response = await API.main.getTopCompanies();
        const companies = response.data.data;
        renderTopCompanies(companies);
    } catch (error) {
        console.error('TOP 10 기업 로드 실패:', error);
    }

    // 주목받는 채용공고 로드 (기본: 스크랩순)
    loadTrendingPosts('scrap');

    // 필터 버튼 이벤트 리스너
    document.querySelectorAll('.filter-buttons button').forEach(button => {
        button.addEventListener('click', function() {
            // 활성 버튼 스타일 변경
            document.querySelector('.filter-buttons button.active').classList.remove('active');
            this.classList.add('active');
            
            // 데이터 로드
            loadTrendingPosts(this.dataset.sort);
        });
    });
});


// 렌더링 함수들
function renderPopularPosts(posts) {
    const container = document.getElementById('popularPosts');
    container.innerHTML = posts.map(post => `
        <div class="post-card">
            <img src="${post.postThumbnail || '/images/default-post.png'}" alt="채용공고 썸네일">
            <div class="post-info">
                <h3>${post.title}</h3>
                <p class="company-name">${post.companyName}</p>
                <p class="skill-tag">${post.skill_name}</p>
                <p class="apply-count">지원자 ${post.apply_count}명</p>
            </div>
        </div>
    `).join('');
}

function renderTopCompanies(companies) {
    const container = document.getElementById('topCompanies');
    container.innerHTML = companies.map(company => `
        <div class="company-card">
            <img src="${company.companyImage || '/images/default-company.png'}" alt="기업 로고">
            <h3>${company.companyName}</h3>
            <div class="rating">
                <span class="stars">★★★★★</span>
                <span class="score">${company.avg_rating.toFixed(1)}</span>
                <span class="review-count">(${company.review_count})</span>
            </div>
        </div>
    `).join('');
}

async function loadTrendingPosts(sortBy) {
    try {
        const response = await API.main.getTrendingPosts(sortBy);
        const posts = response.data.data;
        const container = document.getElementById('trendingPosts');
        
        container.innerHTML = posts.map(post => `
            <div class="post-card">
                <img src="${post.postThumbnail || '/images/default-post.png'}" alt="채용공고 썸네일">
                <div class="post-info">
                    <h3>${post.title}</h3>
                    <p class="company-name">${post.companyName}</p>
                    <p class="stats">
                        ${sortBy === 'scrap' 
                            ? `<i class="fas fa-bookmark"></i> ${post.scrap_count}`
                            : `<i class="fas fa-eye"></i> ${post.viewCnt}`}
                    </p>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('주목받는 채용공고 로드 실패:', error);
    }
} 