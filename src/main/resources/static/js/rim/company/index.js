window.company = {
    async init() {
        try {
            const response = await API.main.getData();
            const companyData = response.userSection?.company;
            if (!companyData) return;

            this.renderProfile(companyData.profile);
            this.renderStats(companyData.stats);
            this.renderActivePosts(companyData.activePosts);
            this.renderCandidates(companyData.recommendedCandidates);
            this.renderRating(companyData.rating);
        } catch (error) {
            console.error('기업 메인 페이지 로딩 실패:', error);
        }
    },
    // ... 기존 메서드들
};

document.addEventListener('DOMContentLoaded', () => {
    window.company.init();
}); 