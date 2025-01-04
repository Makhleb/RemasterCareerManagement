// DOM 요소 업데이트
const DOM = {
    guestBanner: document.getElementById('guestBanner'),
    popularPosts: document.getElementById('popularPosts'),
    topCompanies: document.getElementById('topCompanies'),
    trendingPosts: document.getElementById('trendingPosts'),
    companyFilterButtons: document.querySelectorAll('.top-companies .filter-buttons button'),
    trendingFilterButtons: document.querySelectorAll('.trending-posts .filter-buttons button'),
    prevButton: document.getElementById('slideLeft'),
    nextButton: document.getElementById('slideRight'),
    companiesSlider: document.getElementById('topCompanies')
};

// 전역 변수로 데이터 저장
let mainPageData = null;

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
        
        mainPageData = response;

        const userSection = response.userSection;
        
        // 인기 기술스택 렌더링
        if (response.popularSkills) {
            renderPopularSkills(response.popularSkills);
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

//구직자 대시보드
// 최근 지원 내역 렌더링
function renderDashboard(dashboardData) {
    const dashboardContainer = document.getElementById('dashboardContainer');
    if (!dashboardContainer) return;

    // 통계 데이터 렌더링
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

    dashboardContainer.innerHTML = dashboardHtml;
}
// 상태 클래스 반환
function getStatusClass(status) {
    switch(status) {
        case 'W': return 'waiting';
        case 'Y': return 'accepted';
        case 'N': return 'rejected';
        default: return 'waiting';
    }
}

// 상태 텍스트 반환
function getStatusText(status) {
    switch(status) {
        case 'W': return '진행중';
        case 'Y': return '합격';
        case 'N': return '불합격';
        default: return '확인중';
    }
}



//구직자 - 마감임박 공고
function renderDeadlinePosts(deadlinePosts) {
    const deadlineContainer = document.getElementById('deadlinePosts');
    if (!deadlineContainer) return;

    const postsHtml = deadlinePosts.map(post => `
        <div class="job-post-card">
            <img src="${post.companyImage}" alt="${post.companyName}" class="company-logo">
            <h3 class="post-title">${post.title || '제목 없음'}</h3>
            <p>${post.companyName}</p>
            <p>${post.benefits ? post.benefits.join(', ') : '복리후생 정보 없음'}</p>
        </div>
    `).join('');

    deadlineContainer.innerHTML = postsHtml;
}



//구직자- 추천공고 랜더링
function renderRecommendedPosts(recommendedPosts) {
    const recommendedContainer = document.getElementById('recommendedPosts');
    if (!recommendedContainer) return;

    const postsHtml = recommendedPosts.map(post => `
        <div class="job-post-card">
            <img src="${post.companyImage}" alt="${post.companyName}" class="company-logo">
            <h3 class="post-title">${post.title || '제목 없음'}</h3>
            <p>${post.companyName}</p>
            <p>${post.benefits ? post.benefits.join(', ') : '복리후생 정보 없음'}</p>
        </div>
    `).join('');

    recommendedContainer.innerHTML = postsHtml;
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
                    <p class="company-name">${post.companyName}</p>
                        <h3 class="post-title">${post.title || '제목 없음'}</h3>
                    
                </div>
                <div class="post-info" style="background-image: url('${post.postThumbnail}')">
                    <div class="post-overlay">
                        <div class="post-tags">
                            <span class="post-tag">💸연봉 ${post.jobSalary || '정보 없음'}만원</span>
                        </div>
                        <div class="bottom-tags">
                            <div class="tag-group">
                                ${skill}
                                ${benefit}
                            </div>
                            <span class="post-tag date-tag">${formatDate(post.endDate)} 
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

// TOP 10 기업 렌더링
function renderTopCompanies(companies) {
    if (!DOM.topCompanies) return;
    
    const companiesHtml = companies.map(company => `
        <div class="company-card">
            <img src="${company.companyImage}" 
                 alt="${company.companyName}"
                 onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${encodeURIComponent(company.companyName)}&size=50&background=random'">
            <h3>${company.companyName}</h3>
            <div class="company-stats">
                <div class="rating">
                    <i class="fas fa-star"></i>
                    <span>${company.avgRating?.toFixed(1) || '0.0'}</span>
                </div>
                ${company.likeCount ? `
                    <div class="like-count">
                        <i class="fas fa-heart"></i>
                        <span>${company.likeCount}명이 관심</span>
                    </div>
                ` : ''}
            </div>
        </div>
    `).join('');

    DOM.topCompanies.innerHTML = companiesHtml;
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


// 기업 TOP 10 필터 버튼 이벤트 핸들러
DOM.companyFilterButtons.forEach(button => {
    button.addEventListener('click', () => {
        if (!mainPageData || !mainPageData.userSection) {
            console.error('데이터가 없습니다');
            return;
        }

        const sortType = button.dataset.sort;

        // 활성 버튼 스타일 변경
        DOM.companyFilterButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');

        // 저장된 데이터에서 정렬
        const companies = mainPageData.userSection.guest?.topCompanies ||
            mainPageData.userSection.jobSeeker?.topCompanies || [];

        // 정렬 로직
        const sortedCompanies = [...companies].sort((a, b) => {
            if (sortType === 'rating') {
                return b.avgRating - a.avgRating;
            } else if (sortType === 'interest') {
                return b.likeCount - a.likeCount;
            }
            return 0;
        });

        renderTopCompanies(sortedCompanies);
    });
});

// 주목받는 채용공고 필터 버튼 이벤트 핸들러
// DOM.trendingFilterButtons.forEach(button => {
//     button.addEventListener('click', () => {
//         if (!mainPageData || !mainPageData.userSection) {
//             console.error('데이터가 없습니다');
//             return;
//         }
//
//         const sortType = button.dataset.sort;
//
//         // 활성 버튼 스타일 변경
//         DOM.trendingFilterButtons.forEach(btn => btn.classList.remove('active'));
//         button.classList.add('active');
//
//         // 저장된 데이터에서 정렬
//         const posts = mainPageData.userSection.guest?.scrapedPosts ||
//             mainPageData.userSection.jobSeeker?.scrapedPosts || [];
//
//         // 정렬 로직
//         const sortedPosts = [...posts].sort((a, b) => {
//             if (sortType === 'scrap') {
//                 return b.scrapCount - a.scrapCount;
//             }
//             return 0;
//         });
//
//         renderTrendingPosts(sortedPosts);
//     });
// });

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