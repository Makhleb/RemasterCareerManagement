document.addEventListener('DOMContentLoaded', async function() {
    try {
        const user = await auth.getCurrentUser();
        console.log('user>>>',user)
        const headerRight = document.querySelector('.header-right');
        
        if (user && user.type !== 'guest') {
            // 로그인 상태
            headerRight.innerHTML = `
                <div id="header-right-search">🔍</div>
                <div id="header-right-cr">
                    <div id="header-right-user">${user.name || '사용자'}님</div>
                    <div id="header-right-logout">로그아웃</div>
                </div>
                <div id="header-right-mypage">${user.type === 'company' ? '기업 마이페이지' : '마이페이지'}</div>
            `;

            // 로그아웃 이벤트 리스너
            document.querySelector('#header-right-logout').addEventListener('click', async () => {
                const isLoggedOut = await auth.logout();
                if (!isLoggedOut) {
                    alert('로그아웃 처리 중 오류가 발생했습니다. 다시 시도해주세요.');
                }
            });

            // 마이페이지 이벤트 리스너
            document.querySelector('#header-right-mypage').addEventListener('click', () => {
                location.href = user.type === 'company' ? '/company/mypage' : '/user/mypage';
            });
        } else {
            // 비로그인 상태
            headerRight.innerHTML = `
                <div id="header-right-search">🔍</div>
                <div id="header-right-cr">
                    <div id="header-right-signup">회원가입</div>
                    <div id="header-right-login">로그인</div>
                </div>
                <div id="header-right-company-services">기업서비스</div>
            `;

            // 로그인/회원가입 이벤트 리스너
            document.querySelector('#header-right-login').addEventListener('click', () => {
                location.href = '/login';
            });
            document.querySelector('#header-right-signup').addEventListener('click', () => {
                location.href = '/signup';
            });
            document.querySelector('#header-right-company-services').addEventListener('click', () => {
                location.href = '/company/signup';
            });
        }
    } catch (error) {
        console.error('사용자 정보 로드 실패:', error);
    }
});