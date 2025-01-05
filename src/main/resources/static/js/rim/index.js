// 공통 기능을 window 객체에 namespace로 관리
window.common = {
    // DOM 요소 추가
    elements: {
    popularPosts: document.getElementById('popularPosts'),
    topCompanies: document.getElementById('topCompanies'),
    trendingPosts: document.getElementById('trendingPosts'),
        companiesSlider: document.getElementById('topCompanies'),
    prevButton: document.getElementById('slideLeft'),
    nextButton: document.getElementById('slideRight'),
        companyFilterButtons: document.querySelectorAll('.top-companies .filter-buttons button')
    },

    // 검색 관련 기능
    search: {
        elements: {
            input: document.getElementById('mainSearch'),
            icon: document.getElementById('searchIcon')
        },

        perform() {
            const keyword = this.elements.input.value.trim();
            if (keyword) {
                window.location.href = `/view/users/job-post/list?keyword=${encodeURIComponent(keyword)}`;
            }
        },

        init() {
            this.elements.input.addEventListener('keyup', (event) => {
                if (event.key === 'Enter') {
                    this.perform();
                }
            });

            this.elements.icon.addEventListener('click', () => {
                this.perform();
            });
        }
    },
        // 공통 섹션 렌더링
        renderCommonSection(commonSection) {
            if (!commonSection?.menuIcons) return;
    
            const menuContainer = document.querySelector('.menu-icons');
            if (!menuContainer) return;
    
            const menuHtml = commonSection.menuIcons.map(icon => `
                <div class="menu-icon ${icon.requireLogin ? 'require-login' : ''}" 
                     data-link="${icon.link}">
                    <i class="fas fa-${icon.icon}"></i>
                    <span>${icon.name}</span>
                </div>
            `).join('');
    
            menuContainer.innerHTML = menuHtml;
    
            // 메뉴 아이콘 클릭 이벤트 처리
            menuContainer.querySelectorAll('.menu-icon').forEach(icon => {
                icon.addEventListener('click', () => {
                    const requireLogin = icon.classList.contains('require-login');
                    const link = icon.dataset.link;
    
                    if (requireLogin && !isLoggedIn()) {
                        alert('로그인이 필요한 서비스입니다.');
                        window.location.href = '/login';
                        return;
                    }
    
                    if (link) {
                        window.location.href = link;
                    }
                });
            });
        },

    // 비회원 섹션 렌더링 관련 기능
    guestSection: {
        // 데이터 저장용 변수
        data: null,
        elements: null, // elements 참조 저장

        render(guestData) {
            // 데이터 저장
            this.data = guestData;
            this.elements = window.common.elements;
            
            this.renderPopularPosts(guestData.popularPosts || []);
            this.renderTopCompanies(guestData.topCompanies || []);
            this.renderTrendingPosts(guestData.scrapedPosts || []);
            this.initCompanyFilter();
            
        },

        // 기업 필터 초기화
        initCompanyFilter() {
            const filterButtons = document.querySelectorAll('.top-companies .filter-buttons button');
            filterButtons.forEach(button => {
                button.addEventListener('click', () => this.handleCompanyFilter(button));
            });
        },

        // 기업 필터 핸들러
        handleCompanyFilter(clickedButton) {
            if (!this.data?.topCompanies) {
                console.error('기업 데이터가 없습니다');
                return;
            }

            const sortType = clickedButton.dataset.sort;

            // 활성 버튼 스타일 변경
            document.querySelectorAll('.top-companies .filter-buttons button')
                .forEach(btn => btn.classList.remove('active'));
            clickedButton.classList.add('active');

            // 저장된 데이터에서 정렬
            const companies = [...this.data.topCompanies];
            
            // 정렬 로직
            const sortedCompanies = companies.sort((a, b) => {
                if (sortType === 'rating') {
                    return b.avgRating - a.avgRating;
                } else if (sortType === 'interest') {
                    return b.likeCount - a.likeCount;
                }
                return 0;
            });

            this.renderTopCompanies(sortedCompanies);
        },

        renderPopularPosts(posts) {
            const container = document.getElementById('popularPosts');
            if (!container) return;
            container.innerHTML = posts.map(this.renderJobPostCard).join('');
        },

        renderTopCompanies(companies) {
            const container = document.getElementById('topCompanies');
            if (!container) return;
            container.innerHTML = companies.map(this.renderCompanyCard).join('');
            this.initializeSlider();
        },

        renderTrendingPosts(posts) {
            const container = document.getElementById('trendingPosts');
            if (!container) return;
            container.innerHTML = posts.map(this.renderJobPostCard).join('');
        },

    // 카드 렌더링 헬퍼 함수 수정
    renderJobPostCard(post) {
        if (!post) return '';
        
        // 복리후생 태그 생성 (첫 번째 항목만)
        const benefit = post.benefits && post.benefits[0] 
            ? `<span class="post-tag benefit-tag small">💝${post.benefits[0]}</span>` 
            : '';
        
        // 기술스택 태그 생성 (최대 2개)
        const skillTags = post.skillCodes && post.skillCodes
            .slice(0, 2)
            .map(skill => `<span class="post-tag skill-tag small">💻${skill}</span>`)
            .join('') || '';

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

        // 스크랩 버튼 클래스 설정
        const scrapBtnClass = post.isScraped ? 'scrap-btn active colorstar' : 'scrap-btn uncolorstar';
        const bookmarkClass = post.isScraped ? 'fa-solid fa-bookmark' : 'fa-regular fa-bookmark';

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
                                ${skillTags}
                                ${benefit}
                            </div>
                            <span class="post-tag date-tag">${formatDate(post.endDate)} 
                                <button class="${scrapBtnClass}" onclick="handleScrap(${post.jobPostNo}, event)">
                                    <i class="${bookmarkClass}"></i>
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        `;
    },

        renderCompanyCard(company) {
            if (!company) return '';
            
            // 정렬 기준에 따라 다른 카드 내용 표시
            const sortType = document.querySelector('.filter-buttons button.active')?.dataset.sort;
            
            return `
                <div class="company-card">
                    <div class="company-info">
                        <img src="${company.companyImage}" 
                             alt="${company.companyName}"
                             onerror="this.onerror=null; this.src='https://ui-avatars.com/api/?name=${encodeURIComponent(company.companyName)}&background=random'">
                        <div class="company-details">
                            <h3>${company.companyName || '회사명 없음'}</h3>
                            <div class="company-stats">
                                ${sortType === 'rating' ? `
                                    <span class="rating">
                                        평균 별점 <i class="fas fa-star"></i> 
                                        ${(company.avgRating || 0).toFixed(1)}
                                    </span>
                                    <span class="jobs-count">
                                        <i class="fas fa-briefcase"></i>
                                        채용 ${company.activeJobCount || 0}건
                                    </span>
                                ` : `
                                    <span class="like-count">
                                        <i class="fas fa-heart"></i>
                                        관심기업 ${company.likeCount || 0}
                                    </span>
                                    <span class="jobs-count">
                                        <i class="fas fa-briefcase"></i>
                                        채용 ${company.activeJobCount || 0}건
                                    </span>
                                `}
                            </div>
                        </div>
                    </div>
                </div>
            `;
        },  

// 슬라이더 초기화
        initializeSlider() {
            const companiesSlider = document.getElementById('topCompanies');
            if (!companiesSlider) return;

    let currentSlide = 0;
            const totalSlides = Math.ceil(companiesSlider.children.length / 5);
    
    // 슬라이드 이동 함수
            const moveSlide = (direction) => {
        currentSlide = (currentSlide + direction + totalSlides) % totalSlides;
        const offset = currentSlide * -100;
                companiesSlider.style.transform = `translateX(${offset}%)`;
            };
    
    // 이벤트 리스너 등록
            const prevButton = document.getElementById('slideLeft');
            const nextButton = document.getElementById('slideRight');
            
            if (prevButton) prevButton.addEventListener('click', () => moveSlide(-1));
            if (nextButton) nextButton.addEventListener('click', () => moveSlide(1));
        },

        renderTopCompanies(companies) {
            const container = document.getElementById('topCompanies');
            if (!container) return;
            container.innerHTML = companies.map(this.renderCompanyCard).join('');
            this.initializeSlider();
        }
    },

    // 초기화 함수
    init() {
        this.search.init();
        this.initMainPage();
    },

    // 메인 페이지 데이터 로드 및 초기화
    async initMainPage() {
        try {
            const response = await API.main.getData();
            
            // userType에 따라 다른 처리
            switch (response.userType) {
                case 'ROLE_GUEST':
                    this.guestSection.render(response.guestSection);
                    break;
                    
                case 'ROLE_USER':
                    window.jobseeker.init(response);
                    break;
                    
                case 'ROLE_COMPANY':
                    window.company.init(response);
                    break;
            }

            // 공통 섹션 렌더링
            if (response.commonSection) {
                this.renderCommonSection(response.commonSection);
            }

            // 인기 기술스택 렌더링
            if (response.popularSkills) {
                this.renderPopularSkills(response.popularSkills);
            }

        } catch (error) {
            console.error('메인 페이지 로딩 실패:', error);
        }
    },

    // 인기 기술스택 렌더링
    renderPopularSkills(skills) {
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
};

// 전역 스크랩 핸들러 함수 수정
window.handleScrap = async function(jobPostNo, event) {
    event.preventDefault();
    event.stopPropagation();
    
    try {
        // 로그인 상태 확인
        const response = await axios.get('/api/auth/me');
        const isLoggedIn = response.data && response.data.role === 'ROLE_USER';
        
        if (!isLoggedIn) {
            if (confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
                window.location.href = '/login';
            }
            return;
        }
        
        const button = event.target.closest('.scrap-btn');
        const icon = button.querySelector('i');
        const isScraped = button.classList.contains('active');

        // 스크랩 API 호출
        const url = isScraped
            ? `/api/users/like/jpl/remove?jobPostNo=${jobPostNo}`
            : `/api/users/like/jpl/add?jobPostNo=${jobPostNo}`;
            
        const response2 = await axios.get(url);
        
        if (response2 === '굿!') {
            // UI 업데이트
            button.classList.toggle('active');
            button.classList.toggle('uncolorstar');
            
            icon.className = isScraped 
                ? 'fas fa-bookmark' 
                : 'fas fa-bookmark active';
            
            // 알림 메시지
            const message = isScraped ? '스크랩이 취소되었습니다.' : '스크랩되었습니다.';
            alert(message);
        }
    } catch (error) {
        console.error('스크랩 처리 중 오류:', error);
        if (error.response?.status === 401) {
            if (confirm('로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?')) {
                window.location.href = '/login';
            }
        } else {
            alert('스크랩 처리 중 오류가 발생했습니다.');
        }
    }
};
// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    window.common.init();
});
