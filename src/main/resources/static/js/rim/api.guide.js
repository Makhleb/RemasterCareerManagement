/**
 * API 사용 가이드
 * 
 * 1. 기본 사용법 (예시)
 * 
 * 1-1. GET 요청
 * // 기본 조회
 * axios.get('/api/users/profile')
 *    .then(response => console.log(response.data.body))
 *    .catch(error => console.error(error));
 * 
 * // 쿼리 파라미터 사용
 * axios.get('/api/job-posts', {
 *     params: {
 *         page: 1,
 *         size: 10,
 *         keyword: '개발자'
 *     }
 * });
 * 
 * 1-2. POST 요청
 * // 기본 데이터 전송
 * axios.post('/api/auth/login', {
 *     userId: 'test',
 *     password: '1234'
 * })
 *    .then(response => {
 *        const data = response.data.body;
 *        localStorage.setItem('token', data.token);
 *    });
 * 
 * // 파일 업로드
 * const formData = new FormData();
 * formData.append('file', document.getElementById('file').files[0]);
 * axios.post('/api/upload', formData, {
 *     headers: {
 *         'Content-Type': 'multipart/form-data'
 *     }
 * });
 * 
 * 1-3. PUT 요청
 * axios.put('/api/users/profile', {
 *     userName: '홍길동',
 *     email: 'hong@test.com'
 * });
 * 
 * 1-4. DELETE 요청
 * axios.delete('/api/posts/1');
 * 
 * 2. async/await 사용법
 * 
 * 2-1. 기본 사용
 * async function getProfile() {
 *     try {
 *         const response = await axios.get('/api/users/profile');
 *         console.log(response.data.body);
 *     } catch (error) {
 *         console.error(error);
 *     }
 * }
 * 
 * 2-2. 여러 API 순차 호출
 * async function getResumeDetails(resumeId) {
 *     try {
 *         // 1. 이력서 정보 조회
 *         const resume = await axios.get(`/api/resumes/${resumeId}`);
 *         
 *         // 2. 기술 스택 조회
 *         const skills = await axios.get(`/api/resumes/${resumeId}/skills`);
 *         
 *         // 3. 데이터 조합
 *         return {
 *             ...resume.data.body,
 *             skills: skills.data.body
 *         };
 *     } catch (error) {
 *         console.error('이력서 조회 실패:', error);
 *     }
 * }
 * 
 * 2-3. 병렬 API 호출 (Promise.all 사용)
 * async function getDashboardData() {
 *     try {
 *         const [profile, posts] = await Promise.all([
 *             axios.get('/api/users/profile'),
 *             axios.get('/api/posts')
 *         ]);
 *         
 *         return {
 *             profile: profile.data.body,
 *             posts: posts.data.body
 *         };
 *     } catch (error) {
 *         console.error('데이터 조회 실패:', error);
 *     }
 * }
 * 
 * 3. 실제 사용 예시
 * 
 * 3-1. 로그인 폼 처리
 * document.getElementById('loginForm').onsubmit = async (e) => {
 *     e.preventDefault();
 *     
 *     try {
 *         const response = await axios.post('/api/auth/login', {
 *             userId: document.getElementById('userId').value,
 *             password: document.getElementById('password').value
 *         });
 *         
 *         const { token } = response.data.body;
 *         localStorage.setItem('token', token);
 *         location.href = '/dashboard';
 *         
 *     } catch (error) {
 *         alert('로그인 실패: ' + error.response.data.body.message);
 *     }
 * };
 * 
 * 3-2. 게시글 목록 조회 및 표시
 * async function displayPosts() {
 *     const container = document.getElementById('postList');
 *     const loading = document.getElementById('loading');
 *     
 *     try {
 *         loading.style.display = 'block';
 *         
 *         const response = await axios.get('/api/posts', {
 *             params: {
 *                 page: 1,
 *                 size: 10
 *             }
 *         });
 *         
 *         const posts = response.data.body;
 *         
 *         posts.forEach(post => {
 *             container.innerHTML += `
 *                 <div class="post">
 *                     <h3>${post.title}</h3>
 *                     <p>${post.content}</p>
 *                 </div>
 *             `;
 *         });
 *         
 *     } catch (error) {
 *         console.error('게시글 조회 실패:', error);
 *     } finally {
 *         loading.style.display = 'none';
 *     }
 * }
 * 
 * 4. 주의사항
 * 
 * 4-1. 응답 형식
 * {
 *     "status": "SUCCESS",
 *     "body": {
 *         // 실제 데이터
 *     },
 *     "timestamp": "2024-01-01T12:00:00"
 * }
 * 
 * 4-2. 에러 처리
 * - 401: 로그인 페이지로 자동 이동
 * - 403: 이전 페이지로 자동 이동
 * - 다른 에러: alert로 메시지 표시
 * 
 * 4-3. async/await 사용 시
 * - async 함수 내에서만 await 사용 가능
 * - try-catch로 에러 처리 필수
 * - Promise.all은 하나라도 실패하면 전체 실패
 */
