const API = {
    mypage: {
        // 기존 메서드들...
        
        // 평점 수정
        updateScore: (data) => {
            return axios.put('/user/mypage/api/scores', data);
        }
    }
}; 