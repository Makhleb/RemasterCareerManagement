/**
 * API 클라이언트
 * 
 * 기본 사용법:
 * 1. HTML에 axios와 api.js 추가
 * <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
 * <script src="/js/api.js"></script>
 * 
 * 2. API 객체를 통해 호출
 * // 예시:
 * API.auth.login({userId: 'test', password: '1234'})
 *    .then(data => console.log('로그인 성공:', data))
 *    .catch(error => console.error('로그인 실패:', error));
 * 
 * 특징:
 * - 자동 토큰 추가 (localStorage의 'token' 사용)
 * - 에러 자동 처리 (401: 로그인 페이지 이동, 403: 이전 페이지 등)
 * - ApiResponse에서 body 부분 자동 추출
 */

// 기본 설정
// axios.defaults.baseURL = '/api';
axios.defaults.timeout = 10000;
axios.defaults.headers.common['Content-Type'] = 'application/json';

// 요청 인터셉터 - 토큰 자동 추가
axios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 - 에러 처리
axios.interceptors.response.use(
    (response) => {
        return response.data;  // 전체 ApiResponse 반환
    },
    (error) => {
        if (error.response) {
            const errorData = error.response.data;
            
            switch (error.response.status) {
                case 401:
                    localStorage.removeItem('token');
                    alert('로그인이 필요합니다.');
                    window.location.href = '/login';
                    break;
                case 403:
                    alert(errorData.body.message || '접근 권한이 없습니다.');
                    history.back();
                    break;
                case 404:  // 리소스 없음
                    alert(errorData.body.message || '요청하신 정보를 찾을 수 없습니다.');
                    break;
                case 500:  // 서버 에러
                    alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
                    break;
                default:
                    alert(errorData.body.message || '오류가 발생했습니다.');
            }
            return Promise.reject(errorData);
        }
        return Promise.reject(error);
    }
);