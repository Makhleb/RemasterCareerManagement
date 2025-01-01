// API 클라이언트 전역 객체
window.API = {
    // 인증 관련 API
    auth: {
        // 로그인
        login: async (data) => {
            return await axios.post('/api/auth/login', data);
        },
        // 기업 로그인
        companyLogin: async (data) => {
            return await axios.post('/api/auth/company/login', data);
        },
        // 회원가입
        signup: async (data) => {
            return await axios.post('/api/auth/signup', data);
        },
        // 기업 회원가입
        companySignup: async (data) => {
            return await axios.post('/api/auth/company/signup', data);
        },
        // 아이디 중복 체크
        checkDuplicate: async (userId) => {
            return await axios.post('/api/auth/check-duplicate', { userId });
        }
    }
};

// 기본 설정
axios.defaults.headers.common['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true;  // CSRF 토큰 및 세션 쿠키 전송을 위해 필요

// 요청 인터셉터 (디버깅용)
axios.interceptors.request.use(
    (config) => {
        // 디버깅을 위한 요청 정보 로깅
        console.group('API Request');
        console.log('URL:', config.url);
        console.log('Method:', config.method?.toUpperCase());
        console.log('Headers:', config.headers);
        if (config.data) {
            console.log('Request Data:', JSON.stringify(config.data, null, 2));
        }
        console.groupEnd();
        return config;
    },
    (error) => {
        console.error('api.js -> request error:', error);
        return Promise.reject(error);
    }
);

axios.interceptors.response.use(
    response => response,
    error => {
        const errorData = error.response?.data;
        const status = error.response?.status;
        const currentPath = window.location.pathname;

        console.log('api.js -> errorData:', errorData);
        console.log('api.js -> status:', status);
        console.log('api.js -> currentPath:', currentPath);

        switch (status) {
            case 401:  // 인증 실패
                const isLoggedIn = document.cookie.includes('Authorization');
                if (isLoggedIn) {
                    axios.post('/api/auth/logout')
                        .then(() => {
                            window.location.href = '/login';
                        })
                        .catch(() => {
                            alert('로그아웃 실패! 다시 시도해주세요.');
                        });
                } else {
                    alert('로그인이 필요합니다.');
                    window.location.href = '/login';
                }
                break;

            case 403:  // 권한 없음
                alert(errorData?.body || '접근 권한이 없습니다.');
                history.back();
                break;

            case 404:  // 리소스 없음
                alert(errorData?.body || '요청하신 정보를 찾을 수 없습니다.');
                break;

            case 500:  // 서버 에러
                alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
                break;

            default:
                alert(errorData?.body || '오류가 발생했습니다.');
        }
        return Promise.reject(errorData || error);
    }
);


// withCredentials: true 설정으로 쿠키 자동 전송
axios.defaults.withCredentials = true;

