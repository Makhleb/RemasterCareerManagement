// auth.js를 전역 변수로
window.auth = {
    _userInfo: null,
    
    // 비회원 접근 가능 페이지 목록
    publicPages: [
        '/',               // 메인 페이지
        '/login',          // 로그인
        '/signup',         // 회원가입
        '/company/signup', // 기업회원가입
    ],

    // 현재 페이지가 비회원 접근 가능한지 확인
    isPublicPage() {
        return this.publicPages.includes(window.location.pathname);
    },

    // 현재 로그인한 사용자 정보 조회
    async getCurrentUser() {
        try {
            const response = await API.auth.me();
            if (response.status === 'SUCCESS') {
                return response.data;
            }
            return null;
        } catch (error) {
            if (error.response?.status === 401) {
                // 인증 실패 시 로그인 페이지로 리다이렉트
                if (!this.isPublicPage()) {
                    location.href = '/login';
                }
                return null;
            }
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
    },

    // 로그아웃
    async logout() {
        try {
            const response = await API.auth.logout();
            if (response === '' || response === null || response.status === 'SUCCESS') {
                // 로그아웃 성공 시 로그인 페이지로 이동
                location.href = '/login';
                return true;
            }
        } catch (error) {
            console.error('로그아웃 실패:', error);
            return false;
        }
    },

}; 


// 헤더에서 사용자 정보 표시 활용 예시
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