
    document.addEventListener('DOMContentLoaded', async function() {
        try {
            // /api/auth/me로 현재 로그인 상태 확인
            const user = await auth.getCurrentUser();
            console.log('header.js -> user -> ', user);
            if (user) {
                // 로그인 상태
                showUserInfo(user);
            } else {
                // 비로그인 상태
                showGuestInfo();
            }
        } catch (error) {
            console.error('사용자 정보 로드 실패:', error);
            showGuestInfo();
        }
    });

    function showUserInfo(user) {
        const userInfoDiv = document.querySelector('.user-info');
        const headerRightSignup = document.querySelector('.header-right-signup');
        const headerRightLogin = document.querySelector('.header-right-login');
        const userNameDiv = document.querySelector('.user-name');
        const myPageLink = document.querySelector('.my-page');

        userNameDiv.textContent = `${user.name}님`;
        
        // 기업회원과 일반회원 구분하여 마이페이지 링크 설정
        if (user.type === 'company') {
            myPageLink.onclick = () => location.href = '/company/mypage';
        } else {
            myPageLink.onclick = () => location.href = '/user/mypage';
        }

        userInfoDiv.style.display = 'flex';
        headerRightSignup.style.display = 'none';
        headerRightLogin.style.display = 'none';
    }

    function showGuestInfo() {
        const userInfoDiv = document.querySelector('.user-info');
        const guestInfoDiv = document.querySelector('.guest-info');
        
        // UI 전환
        userInfoDiv.style.display = 'none';
        guestInfoDiv.style.display = 'flex';
    }

    // 로그아웃 처리
    document.querySelector('.logout-btn').addEventListener('click', async function() {
        try {
            await auth.logout();
        } catch (error) {
            console.error('로그아웃 실패:', error);
        }
    });