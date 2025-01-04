document.addEventListener('DOMContentLoaded', async function () {
    try {
        const user = await auth.getCurrentUser();
        console.log('user>>>', user)
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

        // 상단 돋보기 모달버전
        const headerRightSearch = document.querySelector("#header-right-search");
        const headerModal = document.querySelector("#header-modal");
        const headerCloseBtn = document.querySelector("#header-close-btn");

// 모달창 열기
        headerRightSearch.addEventListener("click", () => {
            headerModal.style.display = "flex"; // 모달창 보이기
        });

// 모달창 닫기
        headerCloseBtn.addEventListener("click", () => {
            headerModal.style.display = "none"; // 모달창 숨기기
        });

// 검색 기능
        const headerSearchInput = document.querySelector("#header-search-input");
        const headerModalSearchButton = document.querySelector("#header-modal-search");

        headerModalSearchButton.addEventListener("click", () => {
            const keyword = headerSearchInput.value.trim();
            if (keyword) {
                window.location.href = `/view/users/job-post/list?keyword=${encodeURIComponent(keyword)}`;
            } else {
                alert("검색어를 입력하세요.");
            }
        });
    } catch (error) {
        console.error('사용자 정보 로드 실패:', error);
    }
});