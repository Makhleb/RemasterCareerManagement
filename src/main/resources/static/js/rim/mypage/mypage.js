// 마이페이지 관련 기능을 window 객체에 namespace로 관리
window.mypage = {
    // 상태 관리
    state: {
        currentRating: 0,
        currentCompanyId: null,
        applications: [],
        filteredApplications: [], // 필터링된 지원 내역
        currentPage: 1,
        itemsPerPage: 5,
        currentFilter: 'ALL', // 현재 필터 상태
        stats: {
            total: 0,
            inProgress: 0,
            pass: 0,
            fail: 0,
            needReview: 0
        },
        companyRatings: new Map(), // 기업별 평점 저장
        isEditMode: false // 평점 수정 모드 여부
    },

    // DOM 요소
    elements: {
        userName: document.getElementById('user-name'),
        totalApplications: document.getElementById('totalApplications'),
        inProgressCount: document.getElementById('inProgressCount'),
        passCount: document.getElementById('passCount'),
        failCount: document.getElementById('failCount'),
        applicationList: document.getElementById('applicationList'),
        reviewModal: document.getElementById('reviewModal'),
        reviewContent: document.getElementById('reviewContent'),
        closeModalBtn: document.getElementById('closeModal'),
        submitReviewBtn: document.getElementById('submitReview'),
        stars: document.querySelectorAll('.star-rating i'),
        filterTags: document.querySelectorAll('.application-tags .tag')
    },

    // 초기화
    init() {
        this.loadUserData();
        this.setupEventListeners();
    },

    // 사용자 데이터 로드
    async loadUserData() {
        try {
            const user = await auth.getCurrentUser();
            if (user) {
                this.elements.userName.textContent = user.name;
                this.loadApplicationStats();
                this.loadRecentApplications();
            }
        } catch (error) {
            console.error('Error loading user data:', error);
            location.href = '/login';
        }
    },

    // 이벤트 리스너 설정
    setupEventListeners() {
        // 별점 선택 이벤트
        this.elements.stars.forEach(star => {
            star.addEventListener('mouseover', () => {
                const rating = star.dataset.rating;
                this.updateStars(rating);
                this.updateRatingText(rating);
            });

            star.addEventListener('mouseout', () => {
                if (!this.state.currentRating) {
                    this.updateStars(0);
                    this.updateRatingText(0);
                } else {
                    this.updateStars(this.state.currentRating);
                    this.updateRatingText(this.state.currentRating);
                }
            });

            star.addEventListener('click', () => {
                this.state.currentRating = star.dataset.rating;
                this.updateStars(this.state.currentRating);
                this.updateRatingText(this.state.currentRating);
            });
        });

        // 모달 닫기 이벤트
        this.elements.closeModalBtn.addEventListener('click', () => {
            this.elements.reviewModal.style.display = 'none';
        });

        // 평점 제출 이벤트
        this.elements.submitReviewBtn.addEventListener('click', () => this.submitScore());

        // 필터 태그 클릭 이벤트
        this.elements.filterTags.forEach(tag => {
            tag.addEventListener('click', () => {
                const filterType = this.getFilterTypeFromTag(tag);
                this.applyFilter(filterType);
                
                // 활성화된 태그 스타일 변경
                this.elements.filterTags.forEach(t => t.classList.remove('active'));
                tag.classList.add('active');
            });
        });
    },

    // 태그 요소로부터 필터 타입 결정
    getFilterTypeFromTag(tag) {
        if (tag.classList.contains('tag-waiting')) return 'WAITING';
        if (tag.classList.contains('tag-pass')) return 'PASS';
        if (tag.classList.contains('tag-fail')) return 'FAIL';
        if (tag.classList.contains('tag-need-review')) return 'NEED_REVIEW';
        return 'ALL';
    },

    // 필터 적용
    applyFilter(filterType) {
        this.state.currentFilter = filterType;
        this.state.currentPage = 1; // 페이지 초기화

        // 필터링 로직
        this.state.filteredApplications = this.state.applications.filter(app => {
            switch (filterType) {
                case 'WAITING':
                    return app.status === 'WAITING';
                case 'PASS':
                    return app.status === 'PASS';
                case 'FAIL':
                    return app.status === 'FAIL';
                case 'NEED_REVIEW':
                    return app.status === 'PASS' && !app.hasScore;
                default:
                    return true; // 'ALL'인 경우 모든 항목 표시
            }
        });

        this.renderCurrentPage();
    },

    // 지원 현황 통계 로드
    async loadApplicationStats() {
        try {
            const response = await API.mypage.getStats();
            const stats = response.data;
            
            // 기본 통계 설정
            this.state.stats = {
                total: stats.total,
                inProgress: stats.inProgress,
                pass: stats.pass,
                fail: stats.fail,
                needReview: 0
            };

            // 평점 미등록 건수 계산 (applications 데이터 필요)
            if (this.state.applications.length > 0) {
                this.updateNeedReviewCount();
            }

            // UI 업데이트
            this.updateStatsUI();
        } catch (error) {
            console.error('Error loading application stats:', error);
        }
    },

    // 최근 지원 내역 로드
    async loadRecentApplications() {
        try {
            const response = await API.mypage.getRecentApplications();
            this.state.applications = response.data.applications || [];
            this.state.filteredApplications = []; // 필터 초기화
            
            this.updateNeedReviewCount();
            this.updateStatsUI();
            this.renderCurrentPage();
            await this.loadCompanyRatings();
        } catch (error) {
            console.error('Error loading recent applications:', error);
            this.elements.applicationList.innerHTML = '<div class="error-message">데이터를 불러오는데 실패했습니다.</div>';
        }
    },

    // 현점 미등록 건수 계산
    updateNeedReviewCount() {
        this.state.stats.needReview = this.state.applications.filter(app => 
            app.status === 'PASS' && !app.hasScore
        ).length;
    },

    // 통계 UI 업데이트
    updateStatsUI() {
        const stats = this.state.stats;
        this.elements.totalApplications.textContent = stats.total;
        this.elements.inProgressCount.textContent = stats.inProgress;
        this.elements.passCount.textContent = stats.pass;
        this.elements.failCount.textContent = stats.fail;
        document.getElementById('needReviewCount').textContent = stats.needReview;
    },

    // 현재 페이지 렌더링
    renderCurrentPage() {
        const start = (this.state.currentPage - 1) * this.state.itemsPerPage;
        const end = start + this.state.itemsPerPage;
        const currentItems = this.state.filteredApplications.length > 0 
            ? this.state.filteredApplications.slice(start, end)
            : this.state.applications.slice(start, end);

        if (currentItems.length > 0) {
            this.elements.applicationList.innerHTML = `
                <div class="application-items">
                    ${currentItems.map(app => this.renderApplicationItem(app)).join('')}
                </div>
                ${this.renderPagination()}
            `;
            this.setupPaginationEvents();
        } else {
            this.elements.applicationList.innerHTML = '<div class="no-data">해당하는 지원 내역이 없습니다.</div>';
        }
    },

    // 페이지네이션 렌더링
    renderPagination() {
        const applications = this.state.filteredApplications.length > 0 
            ? this.state.filteredApplications 
            : this.state.applications;
        const totalPages = Math.ceil(applications.length / this.state.itemsPerPage);
        if (totalPages <= 1) return '';

        let pages = '';
        for (let i = 1; i <= totalPages; i++) {
            pages += `
                <button class="page-btn ${i === this.state.currentPage ? 'active' : ''}" 
                        data-page="${i}">
                    ${i}
                </button>
            `;
        }

        return `
            <div class="pagination">
                ${pages}
            </div>
        `;
    },

    // 페이지네이션 이벤트 설정
    setupPaginationEvents() {
        const pageButtons = document.querySelectorAll('.page-btn');
        pageButtons.forEach(button => {
            button.addEventListener('click', () => {
                const page = parseInt(button.dataset.page);
                this.state.currentPage = page;
                this.renderCurrentPage();
            });
        });
    },

    // 지원 항목 렌더링
    renderApplicationItem(app) {
        return `
            <div class="application-item">
                <div class="company-info">
                    <div class="company-name">${app.companyName}</div>
                    <div class="job-title">${app.jobTitle}</div>
                    <div class="application-date">지원일시: ${this.formatDate(app.appliedDate)}</div>
                </div>
                <div class="application-status-container">
                    <span class="application-status status-${app.status.toLowerCase()}">
                        ${this.getStatusText(app.status)}
                    </span>
                    ${app.status === 'PASS' ? `
                        ${app.hasScore ? 
                            `<span class="score-registered">평점등록완료</span>` : 
                            `<button class="write-review-btn" 
                                    onclick="mypage.openScoreModal('${app.companyId}', '${app.companyName}')">
                                평점등록
                            </button>`
                        }
                    ` : ''}
                </div>
            </div>
        `;
    },

    // 상태 텍스트 변환
    getStatusText(status) {
        const statusMap = {
            'WAITING': '검토중',
            'PASS': '합격',
            'FAIL': '불합격'
        };
        return statusMap[status] || '확인중';
    },

    // 별점 UI 업데이트
    updateStars(rating) {
        this.elements.stars.forEach((star, index) => {
            if (index < rating) {
                star.className = 'fas fa-star';
            } else {
                star.className = 'far fa-star';
            }
        });
    },

    // 평점 텍스트 업데이트
    updateRatingText(rating) {
        const ratingText = document.querySelector('.rating-text');
        ratingText.textContent = rating ? `${rating}점` : '0점';
    },

    // 평점 모달 열기
    openScoreModal(companyId, companyName, currentScore = 0) {
        this.state.currentCompanyId = companyId;
        this.state.currentRating = currentScore;
        this.state.isEditMode = currentScore > 0;
        
        this.elements.reviewModal.style.display = 'flex';
        document.querySelector('.modal-content h3').textContent = 
            `${companyName} 평점 ${this.state.isEditMode ? '수정' : '등록'}`;
        
        this.updateStars(currentScore);
        this.updateRatingText(currentScore);
        
        // 버튼 텍스트 변경
        this.elements.submitReviewBtn.textContent = this.state.isEditMode ? '수정' : '등록';
    },

    // 평점 제출
    async submitScore() {
        if (!this.state.currentRating) {
            alert('평점을 선택해주세요.');
            return;
        }

        try {
            const scoreData = {
                companyId: this.state.currentCompanyId,
                score: parseInt(this.state.currentRating)
            };

            if (this.state.isEditMode) {
                await API.mypage.updateScore(scoreData);
            } else {
                await API.mypage.createScore(scoreData);
            }

            alert(`평점이 ${this.state.isEditMode ? '수정' : '등록'}되었습니다.`);
            this.elements.reviewModal.style.display = 'none';
            
            // 데이터 새로고침
            await this.loadRecentApplications();
            await this.loadCompanyRatings();
        } catch (error) {
            console.error('Error submitting score:', error);
            alert(`평점 ${this.state.isEditMode ? '수정' : '등록'}에 실패했습니다.`);
        }
    },

    // 기업별 평점 정보 로드 및 렌더링
    async loadCompanyRatings() {
        const ratedCompanies = this.state.applications
            .filter(app => app.status === 'PASS')
            .reduce((companies, app) => {
                if (!companies.has(app.companyId)) {
                    companies.set(app.companyId, {
                        companyId: app.companyId,
                        companyName: app.companyName,
                        hasScore: app.hasScore,
                        score: app.score || 0
                    });
                }
                return companies;
            }, new Map());

        this.state.companyRatings = ratedCompanies;
        this.renderCompanyRatings();
    },

    // 기업 평점 목록 렌더링
    renderCompanyRatings() {
        const ratingsList = document.getElementById('companyRatingsList');
        const companies = Array.from(this.state.companyRatings.values());

        if (companies.length === 0) {
            ratingsList.innerHTML = '<div class="no-data">평가 가능한 기업이 없습니다.</div>';
            return;
        }

        ratingsList.innerHTML = companies.map(company => `
            <div class="company-rating-item">
                <div class="company-info">
                    <div class="company-name">${company.companyName}</div>
                    ${company.hasScore ? `
                        <div class="rating-display">
                            <div class="rating-stars">
                                ${'<i class="fas fa-star"></i>'.repeat(company.score)}
                                ${'<i class="far fa-star"></i>'.repeat(5 - company.score)}
                            </div>
                            <span>${company.score}점</span>
                        </div>
                    ` : '<span class="no-rating">평점 미등록</span>'}
                </div>
                <button class="edit-rating-btn" 
                        onclick="mypage.openScoreModal('${company.companyId}', '${company.companyName}', ${company.score || 0})">
                    ${company.hasScore ? '수정' : '등록'}
                </button>
            </div>
        `).join('');
    },

    // 날짜 포맷팅 함수
    formatDate(dateString) {
        const date = new Date(dateString);
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    }
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    window.mypage.init();
}); 