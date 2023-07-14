package com.cinema.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.cinema.entity.MovieImg;
import com.cinema.repository.MovieImgRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieImgService {

	private String movieImgLocation = "C:/cinema/movie";
	private final MovieImgRepository movieImgRepository;
	private final FileService fileService;

	// 이미지 저장 1.파일을 movieImgLocation에 저장 2.movie_img 테이블에 저장
	public void saveMovieImg(MovieImg movieImg, MultipartFile movieImgFile) throws Exception {
		String oriImgName = movieImgFile.getOriginalFilename(); // 파일이름 -> 이미지1.jpg
		String imgName = "";
		String imgUrl = "";

		// 1.파일을 movieImgLocation에 저장
		if (!StringUtils.isEmpty(oriImgName)) {
			// oriImgName이 빈문자열이 아니라면 이미지 파일 업로드
			imgName = fileService.uploadFile(movieImgLocation, oriImgName, movieImgFile.getBytes());
			imgUrl = "/images/movie/" + imgName;

		}

		// 2.movie_img 테이블에 저장

		// (이미지1.jpg, ERSFHG4FDGD454.jpg, /images/movie/ERSFHG4FDGD454.jpg)로 entity값을
		// update
		movieImg.updateMovieImg(oriImgName, imgName, imgUrl);
		movieImgRepository.save(movieImg); // db에 insert
	}

	// 이미지 업데이트 메소드
	public void updateMovieImg(Long movieImgId, MultipartFile movieImgFile) throws Exception {
		if (!movieImgFile.isEmpty()) { // 첨부한 이미지 파일이 있으면

			// 해당 이미지를 가져오고
			MovieImg savedMovieImg = movieImgRepository.findById(movieImgId).orElseThrow(EntityNotFoundException::new);

			// 기존 이미지 파일 C:/cinema/movie 폴더에서 삭제
			if (!StringUtils.isEmpty(savedMovieImg.getImgName())) {
				fileService.deleteFile(movieImgLocation + "/" + savedMovieImg.getImgName());
			}

			// 수정된 이미지 파일 업로드 C:/cinema/movie에 업로드
			String oriImgName = movieImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(movieImgLocation, oriImgName, movieImgFile.getBytes());
			String imgUrl = "/images/movie/" + imgName;

			// update쿼리문 실행
			/*
			 * 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로 그 이후에는 변경감지 기능이 동작하기 때문에 개발자는
			 * update쿼리문을 쓰지 않고 엔티티 데이터만 변경해주면 된다.
			 */
			savedMovieImg.updateMovieImg(oriImgName, imgName, imgUrl);

		}
	}

}
