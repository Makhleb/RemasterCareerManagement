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

    // 비회원 섹션 렌더링 관련 기능
    guestSection: {
        // 데이터 저장용 변수
        data: null,

        render(guestData) {
            // 데이터 저장
            this.data = guestData;
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

        // 카드 렌더링 헬퍼 함수들
        renderJobPostCard(post) {
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
        },

        renderCompanyCard(company) {
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
        },

        // 슬라이더 초기화
        initializeSlider() {
            const elements = window.common.elements; // DOM 요소 참조
            let currentSlide = 0;
            const totalSlides = Math.ceil(elements.companiesSlider.children.length / 5);
            
            // 슬라이드 이동 함수
            function moveSlide(direction) {
                currentSlide = (currentSlide + direction + totalSlides) % totalSlides;
                const offset = currentSlide * -100;
                elements.companiesSlider.style.transform = `translateX(${offset}%)`;
            }
            
            // 이벤트 리스너 등록
            elements.prevButton.addEventListener('click', () => moveSlide(-1));
            elements.nextButton.addEventListener('click', () => moveSlide(1));
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
            console.log('메인 데이터 응답:', response);
            
            // 비회원 섹션 렌더링
            if (response.userSection?.guest) {
                this.guestSection.render(response.userSection.guest);
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

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    window.common.init();
});
