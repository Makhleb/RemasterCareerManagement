// API 엔드포인트 상수
const API_ENDPOINTS = {
    MAIN: {
        DATA: '/api/main/data',
        POPULAR_POSTS: '/api/main/popular-posts',
        TOP_COMPANIES: '/api/main/top-companies',
        MOST_SCRAPED: '/api/main/most-scraped'
    }
};

// API 요청 객체
window.API = {
    // 기존 auth 관련 API 유지
    auth: {
        // 로그인
        login: async (data) => {
            return await axios.post('/api/auth/login', data);
        },
        // 기업 로그인
        companyLogin: async (data) => {
            return await axios.post('/api/auth/company/login', data);
        },
        // 로그아웃
        logout: async () => {
            return await axios.post('/api/auth/logout');
        },
        // 현재 사용자 정보 조회
        me: async () => {
            return await axios.get('/api/auth/me');
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
        },
        // 기업 아이디 중복 체크
        checkCompanyDuplicate: async (companyId) => {
            return await axios.post('/api/auth/company/check-duplicate', { companyId });
        }
    },

    // 메인 페이지 관련 API 추가
    main: {
        // 메인 페이지 전체 데이터 조회
        getData: async () => {
            try {
                const response = await axios.get(API_ENDPOINTS.MAIN.DATA);
                return response.data;
            } catch (error) {
                console.error('메인 데이터 조회 실패:', error);
                throw error;
            }
        },

        // 인기 채용공고 조회
        getPopularPosts: async () => {
            try {
                const response = await axios.get(API_ENDPOINTS.MAIN.POPULAR_POSTS);
                return response.data;
            } catch (error) {
                console.error('인기 채용공고 조회 실패:', error);
                throw error;
            }
        },

        // TOP 10 기업 조회
        getTopCompanies: async () => {
            try {
                const response = await axios.get(API_ENDPOINTS.MAIN.TOP_COMPANIES);
                return response.data;
            } catch (error) {
                console.error('TOP 10 기업 조회 실패:', error);
                throw error;
            }
        },

        // 스크랩 많은 채용공고 조회
        getMostScrapedPosts: async () => {
            try {
                const response = await axios.get(API_ENDPOINTS.MAIN.MOST_SCRAPED);
                return response.data;
            } catch (error) {
                console.error('스크랩 많은 채용공고 조회 실패:', error);
                throw error;
            }
        }
    }
};

// 기본 설정
axios.defaults.headers.common['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true;  // CSRF 토큰 및 세션 쿠키 전송을 위해 필요

// 요청/응답 로깅 스타일 설정
const logStyles = {
    request: 'color: #4CAF50; font-weight: bold;',  // 초록색
    response: 'color: #2196F3; font-weight: bold;', // 파란색
    error: 'color: #f44336; font-weight: bold;',    // 빨간색
    groupTitle: 'font-size: 12px; font-weight: bold; color: #666;'
};

// 요청 인터셉터
axios.interceptors.request.use(
    (config) => {
        console.groupCollapsed('%c→ Request: ' + config.method.toUpperCase() + ' ' + config.url, logStyles.request);
        console.log('Time:', new Date().toLocaleTimeString());
        console.log('Headers:', config.headers);
        if (config.data) {
            console.log('Payload:', config.data);
        }
        console.groupEnd();
        return config;
    },
    (error) => {
        console.error('Request Error:', error);
        return Promise.reject(error);
    }
);

// 응답 인터셉터
axios.interceptors.response.use(
    response => {
        console.groupCollapsed(
            '%c← Response: ' + response.status + ' ' + response.config.method.toUpperCase() + ' ' + response.config.url,
            response.status < 400 ? logStyles.response : logStyles.error
        );
        console.log('Time:', new Date().toLocaleTimeString());
        console.log('Status:', response.status);
        console.log('Data:', response.data);
        console.groupEnd();
        return response.data;
    },
    error => {
        console.groupCollapsed(
            '%c× Error: ' + (error.response?.status || 'NETWORK') + ' ' + error.config?.method.toUpperCase() + ' ' + error.config?.url,
            logStyles.error
        );
        console.log('Time:', new Date().toLocaleTimeString());
        console.log('Status:', error.response?.status);
        console.log('Error:', error.response?.data || error.message);
        if (error.response?.data?.stack) {
            console.log('Stack:', error.response.data.stack);
        }
        console.groupEnd();

        const errorResponse = error.response?.data;
        
        // 인증 관련 에러 (401)
        
            switch (errorResponse?.code) {
                case 'AUTH_LOGIN_FAILED':
                    // 로그인 실패는 각 컴포넌트에서 처리
                    break;
                    
                case 'AUTH_INVALID_TOKEN':
                    console.log('AUTH_INVALID_TOKEN');
                    break;
                case 'AUTH_TOKEN_EXPIRED':
                    // 토큰 만료/유효하지 않은 경우
                    auth._userInfo = null;  // 사용자 정보 초기화
                    if (!auth.isPublicPage()) {  // 공개 페이지가 아닌 경우만
                        alert('로그인이 만료되었습니다. 다시 로그인해주세요.');
                        location.href = '/login';
                    }
                    break;

                case 'AUTH_UNAUTHORIZED':
                    console.log('AUTH_UNAUTHORIZED : 권한 없음');
                    // 권한 없음 - /api/auth/me 등에서 발생
                    // if (!auth.isPublicPage()) {  // 공개 페이지가 아닌 경우만
                    //     location.href = '/login';
                    // }
                    break;

                default:
                    // 기타 401 에러
                    console.error('인증 에러:', errorResponse);
                    break;
            }
        return Promise.reject(errorResponse || error);
    }
);


// withCredentials: true 설정으로 쿠키 자동 전송
axios.defaults.withCredentials = true;

