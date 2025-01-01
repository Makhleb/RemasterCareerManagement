// auth.js를 전역 변수로
window.auth = {
    _userInfo: null,
    
    // 비회원 접근 가능 페이지 목록
    publicPages: [
        '/',               // 메인 페이지
        '/login',          // 로그인
        '/signup',         // 회원가입
        '/company/signup', // 기업회원가입
        '/search',         // 검색
        '/jobs',          // 채용공고 목록
    ],

    // 현재 페이지가 비회원 접근 가능한지 확인
    isPublicPage() {
        const currentPath = window.location.pathname;
        return this.publicPages.includes(currentPath);
    },

    // 현재 로그인한 사용자 정보 조회
    async getCurrentUser() {
        try {
            // 비회원 페이지면 API 호출 스킵
            if (this.isPublicPage()) {
                return null;
            }

            const response = await axios.get('/api/auth/me');
            if (response && response.success) {
                this._userInfo = response.body;
                console.log('auth.js -> response -> ', response);
                console.log('auth.js -> response.body -> ', response.body);
                console.log('auth.js -> this._userInfo -> ', this._userInfo);   
                return this._userInfo;
            }
            return null;
        } catch (error) {
            console.error('사용자 정보 조회 실패:', error);
            return null;
        }
    },

    // 권한 체크
    async hasRole(role) {
        const user = await this.getCurrentUser();
        return user?.role === role;
    },

    // 기업회원 여부 체크
    async isCompanyUser() {
        const user = await this.getCurrentUser();
        return user?.type === 'company';
    }
}; 


// 헤더에서 사용자 정보 표시 활용
// document.addEventListener('DOMContentLoaded', async function() {
//     // 비회원 페이지면 바로 비회원 UI 표시
//     if (auth.isPublicPage()) {
//         showGuestInfo();
//         return;
//     }

//     try {
//         const user = await auth.getCurrentUser();
//         if (user) {
//             showUserInfo(user);
//         } else {
//             showGuestInfo();
//         }
//     } catch (error) {
//         console.error('사용자 정보 로드 실패:', error);
//         showGuestInfo();
//     }
// });