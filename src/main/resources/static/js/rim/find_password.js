document.addEventListener('DOMContentLoaded', () => {
    const findPasswordForm = document.getElementById('findPasswordForm');
    const resetPasswordForm = document.getElementById('resetPasswordForm');
    const modal = document.getElementById('resetPasswordModal');
    const closeBtn = document.querySelector('.close');
    let foundUserId = null;  // 사용자 ID 저장용

    // 유효성 검사 규칙
    const validationRules = {
        userId: {
            pattern: /^[a-zA-Z0-9]{4,20}$/,
            message: '아이디는 4~20자의 영문자 또는 숫자여야 합니다'
        },
        newPassword: {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
            message: '비밀번호는 영문, 숫자, 특수문자 조합 8자 이상이어야 합니다'
        },
        userPhone: {
            pattern: /^\d{3}-\d{3,4}-\d{4}$/,
            message: '올바른 전화번호 형식이 아닙니다'
        },
        userEmail: {
            pattern: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
            message: '올바른 이메일 형식이 아닙니다'
        }
    };

    // 에러 메시지 표시 함수
    const showError = (fieldName, message) => {
        const errorDiv = document.getElementById(`${fieldName}Error`);
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
        }
    };

    // 에러 메시지 초기화 함수
    const clearErrors = () => {
        document.querySelectorAll('.error-message').forEach(div => {
            div.style.display = 'none';
            div.textContent = '';
        });
    };

    // 비밀번호 찾기 폼 제출
    findPasswordForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        clearErrors();

        const userId = document.getElementById('userId').value;
        const userEmail = document.getElementById('userEmail').value;
        const userPhone = document.getElementById('userPhone').value;

        // 유효성 검사
        if (!validationRules.userId.pattern.test(userId)) {
            showError('userId', validationRules.userId.message);
            return;
        }
        if (!validationRules.userEmail.pattern.test(userEmail)) {
            showError('userEmail', validationRules.userEmail.message);
            return;
        }
        if (!validationRules.userPhone.pattern.test(userPhone)) {
            showError('userPhone', validationRules.userPhone.message);
            return;
        }

        try {
            const response = await API.auth.findPassword({
                userId,
                userEmail,
                userPhone
            });

            if (response.status === 'SUCCESS' && response.data?.userId) {
                foundUserId = response.data.userId;
                document.getElementById('foundUserId').textContent = foundUserId;  // ID 수정
                modal.style.display = 'block';
            }
        } catch (error) {
            console.error('비밀번호 찾기 에러:', error);
            alert(error.message || '일치하는 사용자 정보를 찾을 수 없습니다.');
        }
    });

    // 비밀번호 재설정 폼 제출
    resetPasswordForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        clearErrors();

        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        // 유효성 검사
        if (!validationRules.newPassword.pattern.test(newPassword)) {
            showError('newPassword', validationRules.newPassword.message);
            return;
        }
        if (newPassword !== confirmPassword) {
            showError('confirmPassword', '비밀번호가 일치하지 않습니다');
            return;
        }

        try {
            const response = await API.auth.resetPassword({
                userId: foundUserId,  // 저장된 foundUserId 사용
                newPassword
            });

            if (response.status === 'SUCCESS') {
                alert('비밀번호가 성공적으로 변경되었습니다.');
                window.location.href = '/login';
            }
        } catch (error) {
            console.error('비밀번호 재설정 에러:', error);
            alert('비밀번호 재설정 중 오류가 발생했습니다.');
        }
    });

    // 모달 닫기 버튼
    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });

    // 전화번호 자동 하이픈 추가
    document.getElementById('userPhone').addEventListener('input', function(e) {
        let value = e.target.value.replace(/[^0-9]/g, '');
        if (value.length > 3 && value.length <= 7) {
            value = value.slice(0,3) + '-' + value.slice(3);
        } else if (value.length > 7) {
            value = value.slice(0,3) + '-' + value.slice(3,7) + '-' + value.slice(7,11);
        }
        e.target.value = value;
    });
}); 