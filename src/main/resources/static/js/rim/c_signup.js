// 약관 체크박스 초기 비활성화
document.querySelectorAll('.required-terms').forEach(checkbox => {
    checkbox.disabled = true;  // 체크박스 비활성화
});

let currentTermType = null;  // 현재 보고 있는 약관 타입 저장 - 모달에서 약관 동의 시 어떤 약관에 동의했는지 식별하기 위해 필요


// 파일 관리 객체
const FileManager = {
    // 현재 선택된 로고 파일
    logoFile: null,

    // 파일 업로드 처리
    handleLogoUpload: function(event) {
        const file = event.target.files[0];
        
        if (!file) {
            this.resetLogoUI();
            return;
        }

        // 파일 유효성 검사
        if (!this.validateFile(file)) {
            return;
        }

        // 미리보기 및 UI 업데이트
        this.updateLogoUI(file);
        
        // 로고 파일 저장
        this.logoFile = file;
        ValidationUI.clearErrors('companyLogo');
    },

    // 로고 UI 업데이트
    updateLogoUI: function(file) {
        const preview = document.getElementById('logoPreview');
        const guideIcon = document.getElementById('logoGuideIcon');
        const fileName = document.getElementById('logoFileName');
        const container = document.querySelector('.logo-upload-container');

        // 미리보기 이미지 업데이트
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
            preview.classList.add('has-image');
        };
        reader.readAsDataURL(file);

        // 파일명 표시
        fileName.textContent = file.name;
        fileName.style.display = 'block';
        
        // 아이콘 숨기기
        guideIcon.style.display = 'none';
        
        // 컨테이너 스타일 업데이트
        container.classList.add('has-file');
    },

    // 로고 UI 초기화
    resetLogoUI: function() {
        const preview = document.getElementById('logoPreview');
        const guideIcon = document.getElementById('logoGuideIcon');
        const fileName = document.getElementById('logoFileName');
        const container = document.querySelector('.logo-upload-container');

        preview.style.display = 'none'; 
        preview.classList.remove('has-image');
        guideIcon.style.display = 'block';
        fileName.style.display = 'none';
        fileName.textContent = '';
        container.classList.remove('has-file');
        
        this.logoFile = null;
    },

    // 파일 유효성 검사
    validateFile: function(file) {
        // 파일 크기 제한 (5MB)
        const maxSize = 5 * 1024 * 1024;
        if (file.size > maxSize) {
            ValidationUI.showError('companyLogo', '파일 크기는 5MB 이하여야 합니다.');
            return false;
        }

        // 파일 형식 검사
        if (!file.type.startsWith('image/')) {
            ValidationUI.showError('companyLogo', '이미지 파일만 업로드 가능합니다.');
            return false;
        }

        return true;
    },

    // 이미지 미리보기 업데이트
    updatePreview: function(file) {
        const preview = document.getElementById('logoPreview');
        const reader = new FileReader();
        
        reader.onload = function(e) {
            preview.src = e.target.result;
        };
        
        reader.readAsDataURL(file);
    },

    // 회원가입 완료 후 로고 업로드
    uploadLogo: async function(companyId) {
        console.log('c_signup.js... this.logoFile : ', this.logoFile);
        if (!this.logoFile) return true; // 로고가 없으면 true 반환

        try {
            const success = await API.file.upload(
                this.logoFile,
                'COMPANY_THUMBNAIL',  // fileGubn (SQL의 tblGubn 테이블 참조)
                companyId            // fileRefId
            );

            console.log('c_signup.js... successdata : ', success);

            if (!success) {
                throw new Error('로고 업로드에 실패했습니다.');
            }

            return true;
        } catch (error) {
            console.error('로고 업로드 에러:', error);
            return false;
        }
    }
};


// 약관 관련 객체
const Terms = {
    // 약관 내용 정의
    contents: {
        1: {
            title: '이용약관',
            content: `제1조(목적) 이 약관은 주식회사 2WH(전자상거래 사업자)가 운영하는 2WH 사이버 몰(이하 "몰"이라 한다)에서 제공하는 인터넷 관련 서비스(이하 "서비스"라 한다)를 이용함에 있어 사이버 몰과 이용자의 권리․의무 및 책임사항을 규정함을 목적으로 합니다.

                제2조(정의)
                ① "몰"이란 주식회사 2WH가 재화 또는 용역(이하 "재화 등"이라 함)을 이용자에게 제공하기 위하여 컴퓨터 등 정보통신설비를 이용하여 재화 등을 거래할 수 있도록 설정한 가상의 영업장을 말하며, 아울러 사이버몰을 운영하는 사업자의 의미로도 사용합니다.`
        },
        2: {
            title: '개인정보 수집 및 이용 동의',
            content: `1. 수집하는 개인정보의 항목
                회사는 회원가입, 상담, 서비스 신청 등을 위해 아래와 같은 개인정보를 수집하고 있습니다.
                ο 수집항목 : 이름 , 로그인ID , 비밀번호 , 자택 전화번호 , 자택 주소 , 휴대전화번호 , 이메일 , 서비스 이용기록 , 접속 로그 , 쿠키 , 접속 IP 정보
                ο 개인정보 수집방법 : 홈페이지(회원가입)`
        },
        3: {
            title: '위치기반서비스 이용약관',
            content: `제1조 (목적)
                본 약관은 회사가 제공하는 위치기반서비스와 관련하여 회사와 개인위치정보주체와의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.

                제2조 (약관의 효력 및 변경)
                ① 본 약관은 서비스를 신청한 고객 또는 개인위치정보주체가 본 약관에 동의하고 회사가 정한 소정의 절차에 따라 서비스의 이용자로 등록함으로써 효력이 발생합니다.`
        }
    },

    // 약관 표시 메서드
    show(termType) {
        const modal = document.getElementById('termsModal');
        const modalTitle = document.getElementById('modalTitle');
        const modalContent = document.getElementById('modalContent');
        const checkbox = document.getElementById(`terms${termType}`);

        currentTermType = termType;
        const term = this.contents[termType];

        modalTitle.textContent = term.title;
        modalContent.textContent = term.content;
        modal.style.display = "block";
        checkbox.disabled = false;
    },

    // 약관 동의 처리 메서드
    agree() {
        if (currentTermType) {
            const checkbox = document.getElementById(`terms${currentTermType}`);
            checkbox.checked = true;
            document.getElementById('termsModal').style.display = "none";
        }
    },

        // 약관 동의 검증
    validate() {
        const requiredTerms = document.querySelectorAll('.required-terms');
        const isAllTermsChecked = [...requiredTerms].every(checkbox => checkbox.checked);
        if (!isAllTermsChecked) {
            ValidationUI.showError('terms', '필수 약관에 모두 동의해주세요.');
            return false;
        }
        return true;
    }
};

// 유효성 검사 규칙 객체
const ValidationRules = {
            companyId: {
                pattern: /^[a-zA-Z0-9]{4,20}$/,
                message: "아이디는 영문, 숫자 4~20자여야 합니다"
            },
            companyPw: {
                pattern: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
                message: "비밀번호는 영문, 숫자, 특수문자 조합 8자 이상이어야 합니다"
            },
            companyPwConfirm: {
                validate: (value, formData) => value === formData.companyPw,
                message: "비밀번호가 일치하지 않습니다"
            },
            companyName: {
                pattern: /^.{2,}$/,
                message: "이름은 2자 이상이어야 합니다"
            },
            companyNumber: {
                pattern: /^\d{3}-\d{2}-\d{5}$/,
                message: "올바른 사업자등록번호 형식이 아닙니다"
            },
            companyRepresentative: {
                pattern: /^[가-힣]{2,4}$/,
                message: "대표자명은 한글 2~4자여야 합니다."
            },
            companyContact: {
                pattern: /^\d{2,3}-\d{3,4}-\d{4}$/,
                message: "올바른 전화번호 형식이 아닙니다"
            },
            emailDomain: {
                pattern: /^[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
                message: "올바른 도메인 형식이 아닙니다"
            }
        };

// 유효성 검사 UI 관련 객체
const ValidationUI = {
    showError(field, message) {
        const errorDiv = document.getElementById(`${field}Error`);
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
        }
    },

    clearErrors() {
        document.querySelectorAll('.error-message').forEach(div => {
            div.style.display = 'none';
        });
    }
};

// 폼 유효성 검사 객체
const FormValidator = {
            validateEmail() {
                const emailId = document.getElementById('emailId').value;
                const emailDomain = document.getElementById('emailDomain').value;
                
                if (!emailId) {
                    ValidationUI.showError('companyEmail', '이메일은 필수 입력 항목입니다.');
                    document.getElementById('emailId').focus();
                    return false;
                }

                if (!emailDomain) {
                    ValidationUI.showError('companyEmail', '이메일 도메인은 필수 입력 항목입니다.');
                    document.getElementById('emailDomain').focus();
                    return false;
                }

                if (!ValidationRules.emailDomain.pattern.test(emailDomain)) {
                    ValidationUI.showError('companyEmail', ValidationRules.emailDomain.message);
                    document.getElementById('emailDomain').focus();
                    return false;
                }

                return true;
            },

            validateRequired(formData) {
                const requiredFields = [
                    'companyId', 'companyPw', 'companyPwConfirm', 'companyName', 
                    'companyNumber', 'companyAddress', 'companyContact'
                ];

                for (const field of requiredFields) {
                    if (!formData[field]) {
                        ValidationUI.showError(field, '필수 입력 항목입니다.');
                        return false;
                    }
                }

                return true;
            },

            validateAll(formData) {
                ValidationUI.clearErrors();

                // 1. 아이디 검증
                if (!this.validateCompanyId(formData.companyId)) return false;
                
                // 2. 비밀번호 검증
                if (!this.validatePassword(formData)) return false;
                
                // 3. 회사명 검증
                if (!this.validateCompanyName(formData.companyName)) return false;
                
                // 4. 연락처 검증
                if (!this.validateContact(formData.companyContact)) return false;
                
                // 5. 이메일 검증
                if (!this.validateEmail()) return false;
                
                // 6. 나머지 필수 필드 검증
                if (!this.validateRemainingFields(formData)) return false;
                
                // 7. 약관 동의 검증
                if (!Terms.validate()) return false;

                return true;
            },

            validateCompanyId(companyId) {
                if (!companyId) {
                    ValidationUI.showError('companyId', '필수 입력 항목입니다.');
                    document.getElementById('companyId').focus();
                    return false;
                }

                if (!ValidationRules.companyId.pattern.test(companyId)) {
                    ValidationUI.showError('companyId', ValidationRules.companyId.message);
                    document.getElementById('companyId').focus();
                    return false;
                }

                // 중복확인 여부 검증
                const isValidated = document.getElementById('companyId').getAttribute('data-validated') === 'true';
                if (!isValidated) {
                    ValidationUI.showError('companyId', '아이디 중복확인이 필요합니다.');
                    document.getElementById('companyId').focus();
                    return false;
                }

                return true;
            },
            validationPresident(president) {
                if (!president) {
                    ValidationUI.showError('companyRepresentative', '필수 입력 항목입니다.');
                    document.getElementById('companyRepresentative').focus();
                    return false;
                }

                if (!ValidationRules.companyRepresentative.pattern.test(president)) {
                    ValidationUI.showError('companyRepresentative', ValidationRules.companyRepresentative.message);
                    document.getElementById('companyRepresentative').focus();
                    return false;
                }

                return true;
            },
            validatePassword(formData) {
                if (!formData.companyPw) {
                    ValidationUI.showError('companyPw', '필수 입력 항목입니다.');
                    document.getElementById('companyPw').focus();
                    return false;
                }

                if (!ValidationRules.companyPw.pattern.test(formData.companyPw)) {
                    ValidationUI.showError('companyPw', ValidationRules.companyPw.message);
                    document.getElementById('companyPw').focus();
                    return false;
                }

                if (formData.companyPw !== formData.companyPwConfirm) {
                    ValidationUI.showError('companyPwConfirm', '비밀번호가 일치하지 않습니다.');
                    document.getElementById('companyPwConfirm').focus();
                    return false;
                }

                return true;
            },

            validateCompanyName(companyName) {
                if (!companyName) {
                    ValidationUI.showError('companyName', '필수 입력 항목입니다.');
                    document.getElementById('companyName').focus();
                    return false;
                }

                if (!ValidationRules.companyName.pattern.test(companyName)) {
                    ValidationUI.showError('companyName', ValidationRules.companyName.message);
                    document.getElementById('companyName').focus();
                    return false;
                }

                return true;
            },

            validateContact(contact) {
                if (!contact) {
                    ValidationUI.showError('companyContact', '필수 입력 항목입니다.');
                    document.getElementById('companyContact').focus();
                    return false;
                }

                if (!ValidationRules.companyContact.pattern.test(contact)) {
                    ValidationUI.showError('companyContact', ValidationRules.companyContact.message);
                    document.getElementById('companyContact').focus();
                    return false;
                }

                return true;
            },

            validateRemainingFields(formData) {
                const requiredFields = [
                    'companyNumber',
                    'companyAddress',
                    'companyZonecode',
                    'companyAddressDetail'
                ];

                for (const field of requiredFields) {
                    if (!formData[field]) {
                        ValidationUI.showError(field, '필수 입력 항목입니다.');
                        document.getElementById(field).focus();
                        return false;
                    }
                }

                // 사업자등록번호 형식 검증
                if (!ValidationRules.companyNumber.pattern.test(formData.companyNumber)) {
                    ValidationUI.showError('companyNumber', ValidationRules.companyNumber.message);
                    document.getElementById('companyNumber').focus();
                    return false;
                }

                return true;
            },

            validateCompanyNumber(number) {
                if (!number) {
                    ValidationUI.showError('companyNumber', '사업자등록번호는 필수 입력 항목입니다.');
                    document.getElementById('companyNumber').focus();
                    return false;
                }

                if (!ValidationRules.companyNumber.pattern.test(number)) {
                    ValidationUI.showError('companyNumber', ValidationRules.companyNumber.message);
                    document.getElementById('companyNumber').focus();
                    return false;
                }

                return true;
            },

            validateAddress(addressData) {
                if (!addressData.companyZonecode) {
                    ValidationUI.showError('companyAddress', '우편번호는 필수 입력 항목입니다.');
                    document.getElementById('companyZonecode').focus();
                    return false;
                }

                if (!addressData.companyAddress) {
                    ValidationUI.showError('companyAddress', '주소는 필수 입력 항목입니다.');
                    document.getElementById('companyAddress').focus();
                    return false;
                }

                if (!addressData.companyAddressDetail) {
                    ValidationUI.showError('companyAddress', '상세주소는 필수 입력 항목입니다.');
                    document.getElementById('companyAddressDetail').focus();
                    return false;
                }

                return true;
            }
};

async function checkDuplicate() {
    const companyId = document.getElementById('companyId').value;
    if (!ValidationRules.companyId.pattern.test(companyId)) {
        ValidationUI.showError('companyId', ValidationRules.companyId.message);
        return;
    }

    try {
        const response = await API.auth.checkCompanyDuplicate(companyId);
        console.log('기업 아이디 중복확인 응답:', response);

        if (response.data.status === 'SUCCESS') {
            const isAvailable = response.data.data.success;
            if (isAvailable) {
                ValidationUI.clearErrors();
                alert('사용 가능한 아이디입니다.');
                document.getElementById('companyId').setAttribute('data-validated', 'true');
            } else {
                ValidationUI.showError('companyId', '이미 사용중인 아이디입니다.');
                document.getElementById('companyId').setAttribute('data-validated', 'false');
            }
        }
    } catch (error) {
        console.error('중복확인 에러:', error);
        ValidationUI.showError('companyId', error.message || '중복 확인 중 오류가 발생했습니다.');
    }
}

// 주소 검색 함수도 전역으로 이동
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById('companyZonecode').value = data.zonecode;
            document.getElementById('companyAddress').value = data.address;
            document.getElementById('companyAddressDetail').focus();
        }
    }).open();
}

// 이벤트 리스너 설정
document.addEventListener('DOMContentLoaded', function() {

    // 로고 업로드 이벤트 핸들러 연결
    const logoInput = document.getElementById('companyLogo');
    if (logoInput) {
        console.log('로고 input 요소 찾음');  // 디버깅 로그
        logoInput.addEventListener('change', function(e) {
            console.log('파일 선택 이벤트 발생');  // 디버깅 로그
            FileManager.handleLogoUpload.call(FileManager, e);  // this 바인딩 수정
        });
    } else {
        console.error('로고 input 요소를 찾을 수 없음');  // 에러 로그
    }

    // 약관 관련 이벤트 리스너
    document.querySelectorAll('.terms-view').forEach(button => {
        button.addEventListener('click', function() {
            const termType = this.getAttribute('data-term-type');
            Terms.show(parseInt(termType));
        });
    });



    // companyId input 이벤트 리스너 추가
    document.getElementById('companyId').addEventListener('input', function() {
        this.setAttribute('data-validated', 'false');
        if (this.value) {  // 값이 있을 경우에만
            ValidationUI.showError('companyId', '아이디 중복확인이 필요합니다.');
        } else {
            ValidationUI.clearErrors();  // 값이 없으면 에러메시지 제거
        }
    });

    // 전화번호 자동 하이픈 추가
    document.getElementById('companyContact').addEventListener('input', function(e) {
        let value = e.target.value.replace(/[^0-9]/g, '');
        if (value.length > 3 && value.length <= 7) {
            value = value.slice(0,3) + '-' + value.slice(3);
        } else if (value.length > 7) {
            value = value.slice(0,3) + '-' + value.slice(3,7) + '-' + value.slice(7,11);
        }
        e.target.value = value;
    });

    // 사업자등록번호 자동 하이픈 추가
    document.getElementById('companyNumber').addEventListener('input', function(e) {
        let value = e.target.value.replace(/[^0-9]/g, '');
        if (value.length > 3 && value.length <= 5) {
            value = value.slice(0,3) + '-' + value.slice(3);
        } else if (value.length > 5) {
            value = value.slice(0,3) + '-' + value.slice(3,5) + '-' + value.slice(5,10);
        }
        e.target.value = value;
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
            Terms.show(parseInt(termType));
        });
    });

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
    document.getElementById('signupForm').addEventListener('submit', async (e) => {

        e.preventDefault();

        ValidationUI.clearErrors();

        // 순차적 검증 - 지정된 순서대로
        // 1. 아이디
        if (!FormValidator.validateCompanyId(document.getElementById('companyId').value)) return;
        
        // 2. 비밀번호
        if (!FormValidator.validatePassword({
            companyPw: document.getElementById('companyPw').value,
            companyPwConfirm: document.getElementById('companyPwConfirm').value
        })) return;
        
        // 3. 회사명
        if (!FormValidator.validateCompanyName(document.getElementById('companyName').value)) return;
        
        // 4. 사업자등록번호
        if (!FormValidator.validateCompanyNumber(document.getElementById('companyNumber').value)) return;
        // 4-1. 대표자명
        if (!FormValidator.validationPresident(document.getElementById('companyRepresentative').value)) return;

        // 5. 주소 관련 필드
        if (!FormValidator.validateAddress({
            companyAddress: document.getElementById('companyAddress').value,
            companyZonecode: document.getElementById('companyZonecode').value,
            companyAddressDetail: document.getElementById('companyAddressDetail').value
        })) return;
        
        // 6. 연락처
        if (!FormValidator.validateContact(document.getElementById('companyContact').value)) return;
        
        // 7. 이메일
        if (!FormValidator.validateEmail()) return;
        
        // 8. 약관 동의
        if (!Terms.validate()) return;

        // API 호출을 위한 데이터 준비
        const formData = {
            companyId: document.getElementById('companyId').value,
            companyPw: document.getElementById('companyPw').value,
            companyPwConfirm: document.getElementById('companyPwConfirm').value,
            companyName: document.getElementById('companyName').value,
            companyNumber: document.getElementById('companyNumber').value.replace(/-/g, ''),
            companyAddress: document.getElementById('companyAddress').value,
            companyZonecode: document.getElementById('companyZonecode').value,
            companyAddressDetail: document.getElementById('companyAddressDetail').value,
            companyContact: document.getElementById('companyContact').value,
            companyEmail: `${document.getElementById('emailId').value}@${document.getElementById('emailDomain').value}`,
            companyWebsite: document.getElementById('companyWebsite').value,
            companyBirth: document.getElementById('companyBirth').value,
            companyEmployee: document.getElementById('companyEmployee').value,
            companyProfit: document.getElementById('companyProfit').value,
            companyRepresentative: document.getElementById('companyRepresentative').value
        };

        // API 호출
        try {
            const response = await API.auth.companySignup(formData);
            console.log('기업 회원가입 응답:', response);

            if (response.status === 200) {

                // 로고 업로드
                const logoUploaded = await FileManager.uploadLogo(formData.companyId);
                if (!logoUploaded) {
                    alert('회원가입은 완료되었으나 로고 업로드에 실패했습니다.');
                }

                alert('기업 회원가입이 완료되었습니다.');
                location.href = '/login';
            }
        } catch (error) {
            console.error('기업 회원가입 에러:', error);

            if (error.response.data.code === 'VALIDATION_ERROR') {
                // 유효성 검사 에러 처리
                Object.entries(error.response.data || {}).forEach(([field, message]) => {
                    ValidationUI.showError(field, message);
                });
            } else if (error.response.data.code === 'BAD_REQUEST') {
                // 비즈니스 로직 에러 (중복 이메일 등)
                alert(error.response.data.message);
            } else {
                // 기타 에러
                alert(error.response.data.message || '회원가입 중 오류가 발생했습니다.');
            }
        }
    });
});