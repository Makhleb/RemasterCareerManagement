// DOM 요소
const DOM = {
    guestBanner: document.getElementById('guestBanner'),
    popularPosts: document.getElementById('popularPosts'),
    topCompanies: document.getElementById('topCompanies'),
    trendingPosts: document.getElementById('trendingPosts'),
    sortButtons: document.querySelectorAll('.filter-buttons button'),
    prevButton: document.getElementById('slideLeft'),
    nextButton: document.getElementById('slideRight'),
    companiesSlider: document.getElementById('topCompanies')
};

let testValue = null

// 기본 색상 배열 정의
const PRIMARY_COLORS = [
    'B9B0B2', 
    '929AA2', 
    'cebfa9' 
];

// 랜덤 색상 선택 함수
function getRandomPrimaryColor() {
    const randomIndex = Math.floor(Math.random() * PRIMARY_COLORS.length);
    return PRIMARY_COLORS[randomIndex];
}

// 메인 페이지 초기화
async function initMainPage() {
    try {
        const response = await API.main.getData();
        console.log('메인 데이터 응답:', response);
        
        const userSection = response.data.userSection;
        
        // 인기 기술스택 렌더링
        if (response.data.popularSkills) {
            renderPopularSkills(response.data.popularSkills);
        }

        // 사용자 타입에 따른 섹션 렌더링
        if (userSection.guest) {
            // 비회원
            renderGuestSection(userSection.guest);
            toggleGuestBanner(true);
        } 
        else if (userSection.jobSeeker) {
            // 일반회원
            renderJobSeekerSection(userSection.jobSeeker);
            toggleGuestBanner(false);
        } 
        else if (userSection.company) {
            // 기업회원
            renderCompanySection(userSection.company);
            toggleGuestBanner(false);
        }
        
    } catch (error) {
        console.error('메인 페이지 로딩 실패:', error);
    }
}

// 비회원용 섹션 렌더링
function renderGuestSection(guestData) {
    renderPopularPosts(guestData.popularPosts || []);
    renderTopCompanies(guestData.topCompanies || []);
    renderTrendingPosts(guestData.scrapedPosts || []);
}

// 일반회원용 섹션 렌더링
function renderJobSeekerSection(seekerData) {
    // 대시보드 렌더링
    renderDashboard(seekerData.dashboard);
    // 맞춤 추천 공고
    renderRecommendedPosts(seekerData.recommendedPosts || []);
    // TOP 10 기업
    renderTopCompanies(seekerData.topCompanies || []);
    // 마감 임박 공고
    renderDeadlinePosts(seekerData.deadlinePosts || []);
}

// 기업회원용 섹션 렌더링
function renderCompanySection(companyData) {
    // 기업 프로필
    renderCompanyProfile(companyData.profile);
    // 채용 현황
    renderRecruitmentStats(companyData.stats);
    // 진행중인 공고
    renderActivePosts(companyData.activePosts || []);
    // 추천 인재
    renderRecommendedCandidates(companyData.recommendedCandidates || []);
}

// 채용공고 카드 렌더링
function renderJobPostCard(post) {
    if (!post) return '';
    
    // 복리후생 태그 생성 (첫 번째 항목만)
    const benefit = post.benefits && post.benefits[0] 
        ? `<span class="post-tag benefit-tag small">💝${post.benefits[0]}</span>` 
        : '';
    
    // 기술스택 태그 생성 (첫 번째 항목만)
    const skill = post.skillCodes && post.skillCodes[0]
        ? `<span class="post-tag skill-tag small">💻${post.skillCodes[0]}</span>`
        : '';
    const ddayClass = post.dday <= 0 ? 'deadline-near' : 'deadline-passed';
    const ddayText = post.dday <= 0 ? `D${post.dday}` : '마감';
    
    // 날짜 형식 변환
    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        const month = date.getMonth() + 1;
        const day = date.getDate();
        const dayOfWeek = ['일', '월', '화', '수', '목', '금', '토'][date.getDay()];
        return `~${month}.${day}(${dayOfWeek})`;
    };
    
    return `
        <div class="job-post-card">
            <div class="company-header">
                <img src="${post.companyImage}" 
                     alt="${post.companyName}" 
                     class="company-logo"
                     onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${encodeURIComponent(post.companyName)}&size=40&background=random'">
                <h3 class="post-title">${post.title || '제목 없음'}</h3>
            </div>
            <div class="post-info" style="background-image: url('${post.postThumbnail}')">
                <div class="post-overlay">
                    <div class="post-tags">
                        <span class="post-tag">💸연봉 ${post.jobSalary || '정보 없음'}만원</span>
                    </div>
                    <div class="post-tags bottom-tags">
                        <div class="tag-group">
                            ${skill}
                            ${benefit}
                        </div>
                        <span class="post-tag">${formatDate(post.endDate)} 
                            <button class="scrap-btn" onclick="handleScrap(${post.jobPostNo}, event)">
                                <i class="fas fa-bookmark"></i>
                            </button>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// 스크랩 처리 함수
async function handleScrap(jobPostNo, event) {
    event.preventDefault();
    event.stopPropagation();
    
    try {
        const response = await fetch('/api/scrap/job-post', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ jobPostNo })
        });
        
        const data = await response.json();
        
        if (response.status === 401) {
            alert('로그인이 필요한 서비스입니다.');
            window.location.href = '/login';
            return;
        }
        
        if (data.success) {
            const btn = event.target.closest('.scrap-btn');
            btn.classList.toggle('active');
            // 스크랩 수 업데이트
            const statsContainer = btn.closest('.post-stats');
            const scrapSpan = statsContainer.querySelector('span:nth-child(2)');
            const currentCount = parseInt(scrapSpan.textContent.split(' ')[1]);
            scrapSpan.textContent = `스크랩 ${btn.classList.contains('active') ? currentCount + 1 : currentCount - 1}`;
        }
    } catch (error) {
        console.error('스크랩 처리 실패:', error);
        alert('스크랩 처리 중 오류가 발생했습니다.');
    }
}

// 기업 카드 렌더링
function renderCompanyCard(company) {
    if (!company) return '';
    
    return `
        <div class="company-card">
            <img src="${company.companyImage}" 
                 alt="${company.companyName}"
                 onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${encodeURIComponent(company.companyName)}&background=random'">
            <h3>${company.companyName || '회사명 없음'}</h3>
            <div class="company-stats">
                <span class="rating">★ ${(company.avgRating || 0).toFixed(1)}</span>
            </div>
        </div>
    `;
}

// 인기 채용공고 섹션 렌더링
function renderPopularPosts(posts) {
    DOM.popularPosts.innerHTML = posts.map(renderJobPostCard).join('');
}

// TOP 10 기업 섹션 렌더링
function renderTopCompanies(companies) {
    // 기업 카드 렌더링
    DOM.companiesSlider.innerHTML = companies.map(renderCompanyCard).join('');
    
    // 슬라이드 초기화
    initializeSlider();
}

// 슬라이더 초기화
function initializeSlider() {
    let currentSlide = 0;
    const totalSlides = Math.ceil(DOM.companiesSlider.children.length / 5);
    
    // 슬라이드 이동 함수
    function moveSlide(direction) {
        currentSlide = (currentSlide + direction + totalSlides) % totalSlides;
        const offset = currentSlide * -100;
        DOM.companiesSlider.style.transform = `translateX(${offset}%)`;
    }
    
    // 이벤트 리스너 등록
    DOM.prevButton.addEventListener('click', () => moveSlide(-1));
    DOM.nextButton.addEventListener('click', () => moveSlide(1));
}

// 주목받는 채용공고 섹션 렌더링
function renderTrendingPosts(posts) {
    DOM.trendingPosts.innerHTML = posts.map(renderJobPostCard).join('');
}

// 게스트 배너 토글
function toggleGuestBanner(show) {
    DOM.guestBanner.style.display = show ? 'block' : 'none';
}

// 정렬 버튼 이벤트 핸들러
DOM.sortButtons.forEach(button => {
    button.addEventListener('click', async () => {
        const sortType = button.dataset.sort;
        
        // 활성 버튼 스타일 변경
        DOM.sortButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
        
        try {
            // 스크랩순으로만 정렬
            const posts = await API.main.getMostScrapedPosts();
            renderTrendingPosts(posts);
        } catch (error) {
            console.error('채용공고 정렬 실패:', error);
        }
    });
});

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', initMainPage); 

function renderPopularSkills(skills) {
    const skillTagsContainer = document.querySelector('.skill-tags');
    if (!skillTagsContainer) return;

    const skillTags = skills.slice(0, 3).map(skill => `
        <span class="skill-tag">
            🔥${skill.skillName}
            <span class="count">${skill.postCount}개의 구인공고!</span>
        </span>
    `).join('');

    skillTagsContainer.innerHTML = skillTags;
} 