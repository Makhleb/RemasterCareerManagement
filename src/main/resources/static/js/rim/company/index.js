// 기업 페이지 관련 기능을 window 객체에 namespace로 관리
window.company = {
    // 차트 인스턴스 저장
    chart: null,

    // DOM 요소
    elements: {
        dashboardContainer: document.getElementById('dashboardContainer'),
        recommendedPosts: document.getElementById('recommendedPosts'),
        popularPosts: document.getElementById('popularPosts'),
        companyFilterButtons: document.querySelectorAll('.top-companies .filter-buttons button')
    },

    // 유틸리티 함수들
    utils: {
        getStatusClass(status) {
            switch (status) {
                case 'W': return 'status-waiting';  // 검토중
                case 'Y': return 'status-pass';     // 합격
                case 'N': return 'status-fail';     // 불합격
                default: return 'status-default';   // 기본
            }
        },

        getStatusText(status) {
            switch (status) {
                case 'W': return '검토중';
                case 'Y': return '합격';
                case 'N': return '불합격';
                default: return '확인중';
            }
        },

        formatDate(dateString) {
            const date = new Date(dateString);
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
        },

        getEducationText(code) {
            const educationMap = {
                'H': '고졸',
                'C': '전문대졸',
                'U': '대졸',
                'G': '대학원졸',
                'D': '박사'
            };
            return educationMap[code] || '학력무관';
        }
    },

    // 페이지 초기화
    init() {
        // 상단 메뉴 아이콘 초기화
        this.initMenuIcons();
        
        // API에서 데이터 가져오기
        axios.get('/api/main/data')
            .then(response => {
                const data = response.data.userSection.company;
                this.renderCompanyProfile(data);
                this.renderDashboard(data);
                this.renderRecommendedCandidates(data);
                this.renderRatingStats(data.rating);
            })
            .catch(error => {
                console.error('Error fetching dashboard data:', error);
            });
    },

    // 상단 메뉴 아이콘 초기화
    initMenuIcons() {
        const menuIcons = [
            {
                icon: 'fa-briefcase',
                text: '채용공고 관리',
                link: '/company/mypage/post-list'
            },
            {
                icon: 'fa-users',
                text: '지원자 관리',
                link: '/company/mypage/applicants'
            },
            {
                icon: 'fa-chart-line',
                text: '통계/분석',
                link: '/company/mypage/stats'
            },
            {
                icon: 'fa-building',
                text: '기업정보 관리',
                link: '/company/mypage/profile'
            }
        ];

        const container = document.querySelector('.banner-icons');
        container.innerHTML = menuIcons.map(item => `
            <div class="banner-icon" onclick="location.href='${item.link}'">
                <i class="fas ${item.icon}"></i>
                <p>${item.text}</p>
            </div>
        `).join('');
    },

    // 대시보드 렌더링
    renderDashboard(data) {
        const container = document.getElementById('dashboardContainer');
        if (!container) return;

        container.innerHTML = `
            <div class="stats-chart">
                <h3>일별 지원자 현황</h3>
                <div class="chart-container">
                    <canvas id="dailyStatsChart"></canvas>
                </div>
                <div class="stats-summary">
                    <div class="stat-item">
                        <span class="stat-label">진행중 <br>공고</span>
                        <span class="stat-value">${data.stats.activePostings}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">30일<br>총 지원</span>
                        <span class="stat-value">${data.stats.totalApplications30Days}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">30일 <br>합격</span>
                        <span class="stat-value pass">${data.stats.passCount30Days}</span>
                    </div>
                </div>
            </div>

            <div class="monthly-stats">
                <h3>최근 30일 통계</h3>
                <div class="chart-container">
                    <canvas id="applicationChart"></canvas>
                </div>
                <div class="stats-numbers">
                    <div class="stat-row">
                        <span class="stat-label">합격</span>
                        <span class="stat-value pass">${data.stats.passCount30Days}</span>
                    </div>
                    <div class="stat-row">
                        <span class="stat-label">불합격</span>
                        <span class="stat-value fail">${data.stats.failCount30Days}</span>
                    </div>
                    <div class="stat-row">
                        <span class="stat-label">검토중</span>
                        <span class="stat-value waiting">${data.stats.waitingCount30Days}</span>
                    </div>
                </div>
            </div>

            
            <div class="stats-detail">
                <h3>최근 공고별 지원자</h3>
                <div class="posts-list">
                    ${data.stats.recentPostStats.map(post => `
                        <div class="post-stat-item">
                            <div class="post-info">
                                <span class="post-title">
                                    <i class="fas fa-file-alt"></i>
                                    ${post.title}
                                </span>
                                <span class="post-date">
                                    <i class="far fa-calendar-alt"></i>
                                    ${post.startDate} 등록
                                </span>
                            </div>
                            <div class="post-numbers">
                                <span class="stat-tag total">
                                    <i class="fas fa-users"></i>
                                    ${post.totalApplicants}명
                                </span>
                                <span class="stat-tag waiting">
                                    <i class="fas fa-hourglass-half"></i>
                                    ${post.waitingCount}명 검토중
                                </span>
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>

        `;

        // 차트 렌더링
        this.renderDailyStatsChart(data.stats.dailyApplications);
        this.renderApplicationChart(data.stats);
    },

    // 지원자 현황 차트 렌더링
    renderApplicationChart(stats) {
        const ctx = document.getElementById('applicationChart').getContext('2d');
        
        if (this.chart) {
            this.chart.destroy();
        }

        this.chart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['합격', '불합격', '검토중'],
                datasets: [{
                    data: [
                        stats.passCount30Days,
                        stats.failCount30Days,
                        stats.waitingCount30Days
                    ],
                    backgroundColor: [
                        '#3A5154',  // 합격 - primary-color-2
                        '#B76737',  // 불합격 - primary-color-1
                        '#B9B0B2'   // 검토중 - primary-color-3
                    ],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                cutout: '70%'
            }
        });
    },

    // 일별 지원자 수 차트 렌더링
    renderDailyStatsChart(dailyData) {
        const ctx = document.getElementById('dailyStatsChart').getContext('2d');
        
        // 데이터 가공
        const dates = [...new Set(dailyData.map(d => d.date))];
        const posts = [...new Set(dailyData.map(d => d.jobPostNo))];
        const postTitles = {};
        
        // 공고별 색상 설정
        const colors = [
            `rgba(58, 81, 84, 0.7)`,      // primary-color-2 (#3A5154)
            `rgba(183, 103, 55, 0.7)`,    // primary-color-1 (#b76737)
            `rgba(185, 176, 178, 0.7)`,   // primary-color-3 (#B9B0B2)
            `rgba(146, 154, 162, 0.7)`,   // primary-color-4 (#929AA2)
            `rgba(29, 38, 37, 0.7)`       // primary-color-5 (#1D2625)
        ];
        
        // 데이터셋 생성
        const datasets = posts.map((postNo, index) => {
            const postData = dailyData.filter(d => d.jobPostNo === postNo);
            postTitles[postNo] = postData[0].postTitle;
            
            return {
                label: postData[0].postTitle,
                data: dates.map(date => {
                    const match = postData.find(d => d.date === date);
                    return match ? match.count : 0;
                }),
                backgroundColor: colors[index % colors.length],
                borderColor: colors[index % colors.length].replace('0.7', '1'),
                borderWidth: 1
            };
        });

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: dates,
                datasets: datasets
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'bottom',
                        labels: {
                            boxWidth: 8,
                            padding: 10,
                            color: '#3A5154',  // primary-color-2
                            font: {
                                size: 9
                            }
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return `${context.dataset.label}: ${context.parsed.y}명`;
                            }
                        },
                        titleColor: '#3A5154',  // primary-color-2
                        bodyColor: '#3A5154',   // primary-color-2
                        backgroundColor: 'rgba(255, 255, 255, 0.9)',
                        borderColor: '#B9B0B2', // primary-color-3
                        borderWidth: 1,
                        titleFont: {
                            size: 14
                        },
                        bodyFont: {
                            size: 13
                        }
                    }
                },
                scales: {
                    x: {
                        stacked: true,
                        grid: {
                            display: false
                        },
                        ticks: {
                            color: '#929AA2',  // primary-color-4
                            font: {
                                size: 12
                            }
                        }
                    },
                    y: {
                        stacked: true,
                        beginAtZero: true,
                        ticks: {
                            stepSize: 10,
                            callback: function(value) {
                                return value + '명';
                            },
                            color: '#929AA2',  // primary-color-4
                            font: {
                                size: 9
                            }
                        },
                        grid: {
                            color: 'rgba(185, 176, 178, 0.1)',  // primary-color-3
                            drawBorder: false
                        }
                    }
                }
            }
        });
    },

    // 기업 프로필 렌더링
    renderCompanyProfile(data) {
        const logoContainer = document.getElementById('companyLogo');
        const name = document.getElementById('companyName');
        
        if (data.profile.companyImage) {
            logoContainer.style.backgroundImage = `url(${data.profile.companyImage})`;
            logoContainer.classList.remove('no-logo');
            logoContainer.innerHTML = '';
        } else {
            logoContainer.style.backgroundImage = 'none';
            logoContainer.classList.add('no-logo');
            logoContainer.innerHTML = '로고 이미지를<br>등록해주세요';
            logoContainer.onclick = () => location.href = '/company/mypage';
        }
        
        name.textContent = data.profile.companyName || '기업명 미등록';
    },

    renderRecommendedCandidates(data) {
        const container = document.getElementById('recommendedCandidates');
        if (!container || !data.recommendedCandidates) return;

        const jobPost = data.recommendedCandidates[0];
        
        container.innerHTML = `
            <div class="job-post-info">
                <h3>${jobPost?.jobTitle || '등록된 채용공고가 없습니다'}</h3>
                <a href="/company/post/${jobPost?.jobPostNo}" class="more-link">
                    <span>상세보기</span>
                    <i class="fas fa-chevron-right"></i>
                </a>
                
                <div class="job-post-meta">
                    <span><i class="far fa-calendar"></i> 작성일: ${this.utils.formatDate(jobPost?.startDate)}</span>
                    <span><i class="far fa-clock"></i> 마감까지 ${jobPost?.daysLeft || 0}일</span>
                    <span><i class="far fa-bookmark"></i> 스크랩 ${jobPost?.scrapCount || 0}회</span>
                </div>
                
                <div class="job-post-stats">
                    <div class="stat-badge">
                        <i class="fas fa-briefcase"></i>
                        경력 ${jobPost?.requiredCareer || 0}년
                    </div>
                    <div class="stat-badge">
                        <i class="fas fa-users"></i>
                        지원자 ${jobPost?.totalApplicants || 0}명
                    </div>
                </div>
                
                <div class="skill-tags">
                    ${jobPost?.requiredSkills?.split(',').map(skill => `
                        <span class="skill-tag">${skill.trim()}</span>
                    `).join('') || '필요 기술 없음'}
                </div>
            </div>
            
            <div class="candidates-list">
                ${data.recommendedCandidates.map(candidate => `
                    <div class="candidate-item">
                        <div class="candidate-photo" style="background-image: url('${candidate.photo || ''}');">
                            ${!candidate.photo ? '<i class="fas fa-user"></i>' : ''}
                        </div>
                        <div class="candidate-details">
                            <div class="candidate-header">
                                <span class="candidate-name">${candidate.name}</span>
                                <span class="candidate-match">${Math.round(candidate.matchRate)}% 매칭</span>
                            </div>
                            <div class="candidate-info">
                                <div class="info-item">
                                    <i class="fas fa-graduation-cap"></i>
                                    <span>${this.utils.getEducationText(candidate.education)}</span>
                                </div>
                                <div class="info-item">
                                    <i class="fas fa-briefcase"></i>
                                    <span>${candidate.career}</span>
                                </div>
                            </div>
                            <div class="candidate-skills">
                                ${candidate.skills.split(',').map(skill => `
                                    <span class="skill-tag">${skill.trim()}</span>
                                `).join('')}
                            </div>
                        </div>
                        <a href="/company/candidate/${candidate.resumeNo}" class="candidate-more">
                            <span>상세보기</span>
                            <i class="fas fa-chevron-right"></i>
                        </a>
                    </div>
                `).join('')}
            </div>
        `;
    },

    // 별점 정보 렌더링
    renderRatingStats(rating) {
        const ratingNumber = document.querySelector('.rating-number');
        const starRating = document.querySelector('.star-rating');
        const reviewCount = document.getElementById('reviewCount');
        const recentReviews = document.getElementById('recentReviews');
        const ratingDistribution = rating.ratingDistribution;
        const totalReviews = ratingDistribution.reduce((a, b) => a + b, 0);

        // 평균 평점과 총 리뷰 수 업데이트
        ratingNumber.textContent = rating.averageRating.toFixed(1);
        reviewCount.textContent = totalReviews;

        // 별점 아이콘 렌더링 (0점일 때도 처리)
        const averageRating = rating.averageRating || 0;
        const fullStars = Math.floor(averageRating);
        const hasHalfStar = averageRating % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

        starRating.innerHTML = 
            (fullStars > 0 ? '<i class="fas fa-star"></i>'.repeat(fullStars) : '') +
            (hasHalfStar ? '<i class="fas fa-star-half-alt"></i>' : '') +
            '<i class="far fa-star"></i>'.repeat(emptyStars);

        // 별점 분포 렌더링
        const distributionContainer = document.querySelector('.rating-distribution');
        distributionContainer.innerHTML = ratingDistribution.map((count, index) => {
            const percentage = totalReviews > 0 ? (count / totalReviews * 100) : 0;
            return `
                <div class="rating-bar">
                    <span class="star-label">${5 - index}점</span>
                    <div class="progress-bar">
                        <div class="progress" style="width: ${percentage}%"></div>
                    </div>
                    <span class="count">${count}</span>
                </div>
            `;
        }).join('');

        // 최근 리뷰 렌더링
        recentReviews.innerHTML = rating.recentReviews.length > 0 
            ? rating.recentReviews.map(review => `
                <div class="review-item">
                    <div class="review-rating">
                        ${'<i class="fas fa-star"></i>'.repeat(review.score)}
                        ${review.score < 5 ? '<i class="far fa-star"></i>'.repeat(5 - review.score) : ''}
                    </div>
                    <div class="review-info">
                        <span>${review.userId}</span>
                        <span class="review-date">${review.scoreDate}</span>
                    </div>
                </div>
            `).join('')
            : '<div class="no-reviews">아직 리뷰가 없습니다.</div>';
    }
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    window.company.init();
}); 
