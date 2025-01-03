## 사용법
파일시스템의 메서드는 다음과 같다.
- loadImage: 파일 불러오기
- saveImage: 파일 저장
- deleteImage: 파일 삭제

## 구조
파일관리 시스템은 서버 프로젝트에 파일을 추가하여 관리하는 시스템이다.
기본적으로 **upload**에 저장되어있음.

### upload의 하위폴더 목록
- COMPANY_THUMBNAIL: 기업 대표사진(기업테이블과 연관)
- POST_THUMBNAIL: 공고 대표 사진(공고테이블과 연관)
- RESUME_HEADSHOT: 이력서 증명사진(이력서테이블과 연관)   

각 폴더의 하위폴더에는 숫자가 적혀있다.
이는 테이블의 pk값이다.
> (예시: POST_THUMBNAIL/39 << 공고tbl에서 pk가 39인 값에 들어갈 이미지)

## 파일 불러오기
### 컬럼 설명
- String fileGubn: 이미지 구분값 세가지 하위 폴더 중 하나를 선택합니다.
- String refId: pk값을 넣는곳
> uploads/fileGubn/refId 로 이동한다고 생각하면 편합니다.

### 반환
String 형태로 반환하며 img src에 바로 넣으면 동작합니다.

### 응용
현재 기업,공고,이력서에는 각각 인코딩된 사진값을 넣을 수 있습니다(dto 참고바람).   
목록을 반환받을때 반환받은 pk값을 다시 fileService로 던져서 각 dto에 사진값을 추가합니다.   
```java
//예시 코드: 내 공고 조회
public List<JobPostDto> selectAllJobPost(String companyId) {
    //companyId 인 공고들 조회 
    List<JobPostDto> daoResult = jobPostDao.selectAll(companyId);


    //공고 list for-each
    for (JobPostDto item : daoResult) {

        // 공고 구분값인 "POST_THUMBNAIL", 공고의 pk값을 넣고 이미지값 반환
        String fileValue = fileService.loadImage("POST_THUMBNAIL", String.valueOf(item.getJobPostNo()));

        //개별 dto에 이미지 값 추가
        item.setPostThumbnail(fileValue);
    }
    return daoResult;
}
```
> 추가: 이미지가 없으면 null로 반환됩니다.

## 파일 삭제
파일 조회와 구조 동일합니다.
폴더 삭제 및 하위 이미지 파일 전부 삭제.

## 파일 저장
저장 메서드는 (uploads에 저장 -> 파일테이블에 추가) 두 단계로 진행한다.
파일 저장 파라미터는 두가지이다.
- MultipartFile file: 파일값
- FileDto fileDto: 파일정보 dto

### 사용예시
```javascript
    function imageUpload(postNo) {
        const formData = new FormData();
        const file = $('#image-upload')[0].files[0];

        let fileDto = {
            fileGubn: 'POST_THUMBNAIL',
            fileRefId: postNo,
            fileName: file.name,
            fileExt: file.type,
            instId: 'companyTestNo'
        }

        formData.append('file', file);
        formData.append(
            'fileDto',
            new Blob(
                [JSON.stringify(fileDto)],
                {type: 'application/json'}
            )
        )

        axios.post('/api/common/file', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(response => {
                console.log(response.data);
                if (response.data) {
                    alert("이미지 저장 완료");
                    location.href = "/mypage/company/post-list";
                }
            })
            .catch(error => {
                console.error('파일 업로드 실패:', error);
            });
    }
```
