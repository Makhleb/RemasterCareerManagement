        // 약관 체크박스 초기 비활성화
        document.querySelectorAll('.required-terms').forEach(checkbox => {
            checkbox.disabled = true;  // 체크박스 비활성화
        });

        let currentTermType = null;  // 현재 보고 있는 약관 타입 저장

        // 약관 내용 표시 함수
        function showTerms(termType) {
            currentTermType = termType;
            const modal = document.getElementById('termsModal');
            const modalTitle = document.getElementById('modalTitle');
            const modalContent = document.getElementById('modalContent');
            const checkbox = document.getElementById(`terms${termType}`);

            let title = '';
            let content = '';

            switch(termType) {
                case 1:
                    title = '이용약관';
                    content = `제1조(목적) 이 약관은 주식회사 2WH(전자상거래 사업자)가 운영하는 2WH 사이버 몰(이하 "몰"이라 한다)에서 제공하는 인터넷 관련 서비스(이하 "서비스"라 한다)를 이용함에 있어 사이버 몰과 이용자의 권리․의무 및 책임사항을 규정함을 목적으로 합니다.

                    제2조(정의)
                    ① "몰"이란 주식회사 2WH가 재화 또는 용역(이하 "재화 등"이라 함)을 이용자에게 제공하기 위하여 컴퓨터 등 정보통신설비를 이용하여 재화 등을 거래할 수 있도록 설정한 가상의 영업장을 말하며, 아울러 사이버몰을 운영하는 사업자의 의미로도 사용합니다.`;
                                        break;
                                    case 2:
                                        title = '개인정보 수집 및 이용 동의';
                                        content = `1. 수집하는 개인정보의 항목
                    회사는 회원가입, 상담, 서비스 신청 등을 위해 아래와 같은 개인정보를 수집하고 있습니다.
                    ο 수집항목 : 이름 , 로그인ID , 비밀번호 , 자택 전화번호 , 자택 주소 , 휴대전화번호 , 이메일 , 서비스 이용기록 , 접속 로그 , 쿠키 , 접속 IP 정보
                    ο 개인정보 수집방법 : 홈페이지(회원가입)`;
                                        break;
                                    case 3:
                                        title = '위치기반서비스 이용약관';
                                        content = `제1조 (목적)
                    본 약관은 회사가 제공하는 위치기반서비스와 관련하여 회사와 개인위치정보주체와의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.

                    제2조 (약관의 효력 및 변경)
                    ① 본 약관은 서비스를 신청한 고객 또는 개인위치정보주체가 본 약관에 동의하고 회사가 정한 소정의 절차에 따라 서비스의 이용자로 등록함으로써 효력이 발생합니다.`;
                                        break;
            }

            modalTitle.textContent = title;
            modalContent.textContent = content;
            modal.style.display = "block";

            // 약관 확인 시 체크박스 활성화
            checkbox.disabled = false;
        }
        
        // 약관 동의 처리 함수 . 현재 선택된 약관에 대해 체크박스를 체크하고 모달을 닫는다
        function agreeToTerms() {
            console.log('agreeToTerms 호출');
            console.log('currentTermType:', currentTermType);
            if (currentTermType) {
                const checkbox = document.getElementById(`terms${currentTermType}`);
                checkbox.checked = true;
                
                // 모달 닫기
                const modal = document.getElementById('termsModal');
                modal.style.display = "none";
            }
        }

        // 유효성 검사 규칙 객체
        const validationRules = {
            userId: {
                pattern: /^[a-zA-Z0-9]{4,20}$/,
                message: '아이디는 영문, 숫자 4~20자여야 합니다'
            },
            userPw: {
                pattern: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
                message: '비밀번호는 영문, 숫자, 특수문자 조합 8자 이상이어야 합니다'
            },
            userPwConfirm: {
                validate: (value, formData) => value === formData.userPw,
                message: '비밀번호가 일치하지 않습니다'
            },
            userName: {
                pattern: /^.{2,}$/,
                message: '이름은 2자 이상이어야 합니다'
            },
            userPhone: {
                pattern: /^\d{3}-\d{3,4}-\d{4}$/,
                message: '올바른 전화번호 형식이 아닙니다'
            },
            userEmail: {
                validate: (value) => {
                    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
                    return emailPattern.test(value);
                },
                message: '올바른 이메일 형식이 아닙니다'
            },
            emailDomain: {
                pattern: /^[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
                message: '올바른 도메인 형식이 아닙니다 (예: gmail.com)'
            }
        };

        // 유효성 검사 함수
        const validateField = (fieldName, value, formData) => {
            const rule = validationRules[fieldName];
            if (!rule) return true;

            if (rule.pattern) {
                return rule.pattern.test(value);
            }
            if (rule.validate) {
                return rule.validate(value, formData);
            }
            return true;
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

        // 아이디 검증
        function validateUserId() {
            const userId = document.getElementById('userId').value;
            if (!userId) {
                showError('userId', '아이디는 필수 입력 항목입니다.');
                document.getElementById('userId').focus();
                return false;
            }

            if (!validationRules.userId.pattern.test(userId)) {
                showError('userId', validationRules.userId.message);
                document.getElementById('userId').focus();
                return false;
            }

            // 중복확인 여부 검증
            const isValidated = document.getElementById('userId').getAttribute('data-validated') === 'true';
            if (!isValidated) {
                showError('userId', '아이디 중복확인이 필요합니다.');
                document.getElementById('userId').focus();
                return false;
            }

            return true;
        }

        // 비밀번호 검증
        function validatePassword() {
            const userPw = document.getElementById('userPw').value;
            if (!userPw) {
                showError('userPw', '필수 입력 항목입니다.');
                document.getElementById('userPw').focus();
                return false;
            }

            if (!validationRules.userPw.pattern.test(userPw)) {
                showError('userPw', validationRules.userPw.message);
                document.getElementById('userPw').focus();
                return false;
            }

            const userPwConfirm = document.getElementById('userPwConfirm').value;
            if (!userPwConfirm) {
                showError('userPwConfirm', '필수 입력 항목입니다.');
                document.getElementById('userPwConfirm').focus();
                return false;
            }

            if (userPw !== userPwConfirm) {
                showError('userPwConfirm', '비밀번호가 일치하지 않습니다.');
                document.getElementById('userPwConfirm').focus();
                return false;
            }

            return true;
        }

        // 이름 검증
        function validateName() {
            const userName = document.getElementById('userName').value;
            if (!userName) {
                showError('userName', '필수 입력 항목입니다.');
                document.getElementById('userName').focus();
                return false;
            }

            if (!validationRules.userName.pattern.test(userName)) {
                showError('userName', validationRules.userName.message);
                document.getElementById('userName').focus();
                return false;
            }

            return true;
        }

        // 휴대폰 번호 검증
        function validatePhone() {
            const userPhone = document.getElementById('userPhone').value;
            if (!userPhone) {
                showError('userPhone', '필수 입력 항목입니다.');
                document.getElementById('userPhone').focus();
                return false;
            }

            if (!validationRules.userPhone.pattern.test(userPhone)) {
                showError('userPhone', validationRules.userPhone.message);
                document.getElementById('userPhone').focus();
                return false;
            }

            return true;
        }

        // 나머지 필수 필드 검증
        function validateRemainingFields() {
            const fields = ['emailId', 'emailDomain', 'birthYear', 'birthMonth', 'birthDay', 'userGender'];
            
            for (const field of fields) {
                const value = document.getElementById(field)?.value;
                if (!value) {
                    const errorField = field === 'emailId' || field === 'emailDomain' ? 'userEmail' : field;
                    showError(errorField, '필수 입력 항목입니다.');
                    document.getElementById(field).focus();
                    return false;
                }

                // 이메일 도메인 패턴 검사 추가
                if (field === 'emailDomain' && !validationRules.emailDomain.pattern.test(value)) {
                    showError('userEmail', validationRules.emailDomain.message);
                    document.getElementById(field).focus();
                    return false;
                }
            }

            return true;
        }

        // 약관 동의 검증
        function validateTerms() {
            const requiredTerms = document.querySelectorAll('.required-terms');
            const isAllTermsChecked = [...requiredTerms].every(checkbox => checkbox.checked);
            if (!isAllTermsChecked) {
                showError('terms', '필수 약관에 모두 동의해주세요.');
                return false;
            }
            return true;
        }

        // 아이디 중복확인 함수
        async function checkDuplicate() {
            const userId = document.getElementById('userId').value;
            if (!validationRules.userId.pattern.test(userId)) {
                showError('userId', validationRules.userId.message);
                document.getElementById('userId').focus();
                return;
            }

            try {
                const response = await API.auth.checkDuplicate(userId);
                console.log('중복확인 응답:', response);
                console.log('중복확인 응답(response.data.data):', response.data.data);

                if (response.data.data) {
                    const isAvailable = response.data.data.success;  // data 필드 사용
                    if (isAvailable) {
                        clearErrors();
                        alert('사용 가능한 아이디입니다.');
                        document.getElementById('userId').setAttribute('data-validated', 'true');
                    } else {
                        showError('userId', '이미 사용중인 아이디입니다.');
                        document.getElementById('userId').setAttribute('data-validated', 'false');
                        document.getElementById('userId').focus();
                    }
                }
            } catch (error) {
                console.error('중복확인 에러:', error);
                showError('userId', error.message || '중복 확인 중 오류가 발생했습니다.');
                document.getElementById('userId').focus();
            }
        }

        // userId input에 변경이 있을 때마다 validated 상태를 false로 변경하고 에러 메시지 표시
        document.getElementById('userId').addEventListener('input', function() {
            this.setAttribute('data-validated', 'false');
            if (this.value) {  // 값이 있을 경우에만
                showError('userId', '아이디 중복확인이 필요합니다.');
            } else {
                clearErrors();  // 값이 없으면 에러메시지 제거
            }
        });

        // 모달 닫기 기능
        document.querySelector('.close').addEventListener('click', function() {
            document.getElementById('termsModal').style.display = "none";
        });

        // 모달 외부 클릭 시 닫기
        window.addEventListener('click', function(event) {
            const modal = document.getElementById('termsModal');
            if (event.target == modal) {
                modal.style.display = "none";
            }
        });

        // 약관 보기 버튼 클릭 이벤트
        document.querySelectorAll('.terms-view').forEach(button => {
            button.addEventListener('click', function() {
                const termType = this.getAttribute('data-term-type');
                showTerms(parseInt(termType));
            });
        });

        // 모달의 동의 버튼에 이벤트 리스너 추가
        document.querySelector('.modal-footer .rim-btn').addEventListener('click', agreeToTerms);

        // 체크박스 직접 클릭 방지
        document.querySelectorAll('.required-terms').forEach(checkbox => {
            checkbox.addEventListener('click', function(e) {
                if (this.disabled) {
                    e.preventDefault();
                    alert('약관을 확인하고 동의해주세요.');
                }
            });
        });

        // 폼 제출 처리
        const form = document.getElementById('signupForm');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            clearErrors();

            // 순차적 검증
            if (!validateUserId()) return;
            if (!validatePassword()) return;
            if (!validateName()) return;
            if (!validatePhone()) return;
            if (!validateRemainingFields()) return;
            if (!validateTerms()) return;

            // API 호출을 위한 데이터 준비
            const formData = {
                userId: document.getElementById('userId').value,
                userPw: document.getElementById('userPw').value,
                userPwConfirm: document.getElementById('userPwConfirm').value,
                userName: document.getElementById('userName').value,
                userPhone: document.getElementById('userPhone').value,
                userEmail: `${document.getElementById('emailId').value}@${document.getElementById('emailDomain').value}`,
                userBirth: `${document.getElementById('birthYear').value}${
                    document.getElementById('birthMonth').value.padStart(2, '0')}${
                    document.getElementById('birthDay').value.padStart(2, '0')}`,
                userGender: document.getElementById('userGender').value
            };

            try {
                const response = await API.auth.signup(formData);
                console.log('회원가입 응답:', response);

                if (response.status === 200) {
                    alert('회원가입이 완료되었습니다.');
                    location.href = '/login';
                }
            } catch (error) {
                console.error('회원가입 에러:', error);

                if (error.response.data.code === 'VALIDATION_ERROR') {
                    // 유효성 검사 에러 처리
                    Object.entries(error.response.data.data || {}).forEach(([field, message]) => {
                        showError(field, message);
                    });
                } else {
                    // 기타 에러 처리
                    alert(error.response.data.message || '회원가입 중 오류가 발생했습니다.');
                }
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

        // 생년월일 셀렉트 박스 초기화
        function initBirthSelect() {
            const yearSelect = document.getElementById('birthYear');
            const monthSelect = document.getElementById('birthMonth');
            const daySelect = document.getElementById('birthDay');

            const currentYear = new Date().getFullYear();
            for (let year = currentYear; year >= currentYear - 100; year--) {
                yearSelect.add(new Option(year, year));
            }

            for (let month = 1; month <= 12; month++) {
                monthSelect.add(new Option(month, month.toString().padStart(2, '0')));
            }

            for (let day = 1; day <= 31; day++) {
                daySelect.add(new Option(day, day.toString().padStart(2, '0')));
            }
        }

        // 페이지 로드 시 초기화
        initBirthSelect();