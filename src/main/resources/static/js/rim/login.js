const loginForm = document.getElementById('loginForm');
const tabButtons = document.querySelectorAll('.tab-button');
const loginType = document.getElementById('loginType');

let testValue = null;

// 탭 전환 처리
tabButtons.forEach(button => {
    button.addEventListener('click', () => {

        // 활성 탭 스타일 변경
        tabButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
        
        // 로그인 타입 설정
        loginType.value = button.dataset.tab;
        
        // 플레이스홀더 텍스트 변경
        const idInput = document.getElementById('userId');
        if (button.dataset.tab === 'company') {
            idInput.placeholder = '기업 아이디를 입력하세요';
        } else {
            idInput.placeholder = '아이디를 입력하세요';
        }
    });
});

// 폼 제출 처리
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    let loginError = document.getElementById('userPwError');

    // 에러 메시지 초기화
    loginError.style.display = 'none';

    const formData = {
        userId: document.getElementById('userId').value,
        userPw: document.getElementById('userPw').value
    };

    if(formData.userId === '' || formData.userPw === '') {
        loginError.style.display = 'block';
        loginError.innerText = '아이디와 비밀번호를 입력해주세요.';
        return;
    }

    try {
        // 로그인 API 호출
        const response = await (loginType.value === 'company' 
            ? API.auth.companyLogin(formData)
            : API.auth.login(formData));

        testValue = response;

        // 성공 시 메인 페이지로 이동
        if (response.status === 200) {
            if(testValue.data.data.type === 'user'){
                location.href = "/jobseeker";
            }else{
                location.href = "/company";
            }
            // location.href = '/';
        }
    } catch (error) {
        console.error('로그인 에러:', error);
        console.error('로그인 에러 (error.response.data):', error.response.data);
        console.error('로그인 에러 메세지(error.response.data.message) :', error.response.data.message);

        // 에러 메시지 표시

        if (error.response.data.code === 'AUTH_LOGIN_FAILED') {
            loginError.textContent = error.response.data.message;
        } else {
            loginError.textContent = '로그인 처리 중 오류가 발생했습니다.';
        }

        loginError.style.display = 'block';
        document.getElementById('userPw').focus();
    }
});