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
                        <span class="stat-label">진행중 공고</span>
                        <span class="stat-value">${data.stats.activePostings}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">30일 총 지원</span>
                        <span class="stat-value">${data.stats.totalApplications30Days}</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">30일 합격</span>
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
                        'rgba(75, 192, 192, 0.8)',  // 합격
                        'rgba(255, 99, 132, 0.8)',  // 불합격
                        'rgba(255, 206, 86, 0.8)'   // 검토중
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
                            boxWidth: 12,
                            padding: 15,
                            color: '#3A5154'  // primary-color-2
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
                        borderWidth: 1
                    }
                },
                scales: {
                    x: {
                        stacked: true,
                        grid: {
                            display: false
                        },
                        ticks: {
                            color: '#929AA2'  // primary-color-4
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
                            color: '#929AA2'  // primary-color-4
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
    }
};

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    window.company.init();
}); 
