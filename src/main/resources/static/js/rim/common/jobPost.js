// 채용공고 카드 관련 공통 기능
const JobPost = {
    // 채용공고 카드 렌더링
    renderCard: function(post) {
        if (!post) return '';
        
        const benefit = post.benefits && post.benefits[0] 
            ? `<span class="post-tag benefit-tag small">💝${post.benefits[0]}</span>` 
            : '';
        
        const skill = post.skillCodes && post.skillCodes[0]
            ? `<span class="post-tag skill-tag small">💻${post.skillCodes[0]}</span>`
            : '';

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
                            <span class="post-tag date-tag">${this.formatDate(post.endDate)} 
                                <button class="scrap-btn" onclick="JobPost.handleScrap(${post.jobPostNo}, event)">
                                    <i class="fas fa-bookmark"></i>
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        `;
    },

    // 날짜 포맷팅
    formatDate: function(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        const month = date.getMonth() + 1;
        const day = date.getDate();
        const dayOfWeek = ['일', '월', '화', '수', '목', '금', '토'][date.getDay()];
        return `~${month}.${day}(${dayOfWeek})`;
    },

    // 스크랩 처리
    handleScrap: async function(jobPostNo, event) {
        event.preventDefault();
        event.stopPropagation();
        
        try {
            const response = await fetch('/api/scrap/job-post', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
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
            }
        } catch (error) {
            console.error('스크랩 처리 실패:', error);
            alert('스크랩 처리 중 오류가 발생했습니다.');
        }
    }
};

// 기업 카드 관련 공통 기능
const CompanyCard = {
    render: function(company) {
        return `
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
        `;
    }
}; 