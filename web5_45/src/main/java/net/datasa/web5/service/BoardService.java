package net.datasa.web5.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.domain.dto.BoardDTO;
import net.datasa.web5.domain.dto.LikesDTO;
import net.datasa.web5.domain.dto.ReplyDTO;
import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.LikesEntity;
import net.datasa.web5.domain.entity.MemberEntity;
import net.datasa.web5.domain.entity.ReplyEntity;
import net.datasa.web5.repository.BoardRepository;
import net.datasa.web5.repository.LikesRepository;
import net.datasa.web5.repository.MemberRepository;
import net.datasa.web5.repository.ReplyRepository;
import net.datasa.web5.util.FileManager;

/**
 * 게시판 관련 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final LikesRepository likesRepository;
    private final FileManager fileManager;
    

    /**
     * 게시판 글 저장
     * @param boardDTO 저장할 글 정보
     * @param uploadPath 파일을 저장할 경로
     * @param upload 업로드된 파일 정보
     */

    public void write(BoardDTO boardDTO, String uploadPath, MultipartFile upload) {
        MemberEntity memberEntity = memberRepository.findById(boardDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원아이디가 없습니다."));

        BoardEntity entity = new BoardEntity();
        entity.setMember(memberEntity);
        entity.setTitle(boardDTO.getTitle());
        entity.setContents(boardDTO.getContents());

        log.debug("저장되는 엔티티 : {}", entity);
        //todo : 첨부 파일 처리할 것
        
        if (upload != null && !upload.isEmpty()) {
            try {
                // FileManager의 saveFile 메서드를 사용하여 파일 저장
                String savedFileName = fileManager.saveFile(uploadPath, upload);
                
                // 원래 이름과 저장된 이름을 Entity에 입력
                entity.setOriginalName(upload.getOriginalFilename());
                entity.setFileName(savedFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        if(upload != null && !upload.isEmpty()) {
        	//저장할 경로의 폴더가 없으면 생성
        	File directoryPath = new File(uploadPath);
        	//지금 이 디렉토리 경로가 없는지?
        	if(!directoryPath.isDirectory()) {
        		//경로를 만들어주는 메서드
        		directoryPath.mkdirs();
        	}
        	//새로운 파일명(겹치지않기위한 방법)
        	 //홍길동의 이력서.doc ->20240806_238472sdflkjesljgrlglkdjflsdjtseihldshflsd.doc
        	String originalName = upload.getOriginalFilename();
        	String extension = originalName.substring(originalName.lastIndexOf("."));
        	String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        	String uuidString = UUID.randomUUID().toString();
        	String fileName = dateString + "_" + uuidString + extension;
        	
        	try {
        	//파일 복사
        	File filePath = new File(uploadPath + "/" + fileName);
        	
        	//파일에만 문제있을 때 글저장 여부 선택
        	upload.transferTo(filePath);
        	
        	//원래 이름과 저장된 이름을 Entity에 입력
        	entity.setOriginalName(originalName);
        	entity.setFileName(fileName);
        	}
        	catch (IOException e) {
        		e.printStackTrace();
        		
        	}
        }
        */

        boardRepository.save(entity);
    }
    
    
    /**
     * 게시글 전체 조회
     * @return 글 목록
     */
    public List<BoardDTO> getListAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "boardNum");
        //전체 보기
        List<BoardEntity> entityList = boardRepository.findAll(sort);
        //제목 검색
//        List<BoardEntity> entityList = boardRepository.findByTitleContaining("111", sort);
        //제목 검색
//        List<BoardEntity> entityList = boardRepository.findByTitleContainingOrContentsContaining("111", "111", sort);

        log.debug("전체 글목록 조회 : {}", entityList);

        List<BoardDTO> dtoList = new ArrayList<>();
        for (BoardEntity entity : entityList) {
            BoardDTO dto = BoardDTO.builder()
                    .boardNum(entity.getBoardNum())
                    .memberId(entity.getMember().getMemberId())
                    .memberName(entity.getMember().getMemberName())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .viewCount(entity.getViewCount())
                    .likeCount(entity.getLikeCount())
                    .originalName(entity.getOriginalName())
                    .fileName(entity.getFileName())
                    .createDate(entity.getCreateDate())
                    .updateDate(entity.getUpdateDate())
                    .build();
            dtoList.add(dto);
        }

        return dtoList;
    }

    /**
     * 검색 후 지정한 한페이지 분량의 글 목록 조회
     *
     * @param page        현재 페이지
     * @param pageSize    한 페이지당 글 수
     * @param searchType  검색 대상 (title, contents, id)
     * @param searchWord  검색어
     * @return 한페이지의 글 목록
     */
    public Page<BoardDTO> getList(int page, int pageSize, String searchType, String searchWord) {
        //Page 객체는 번호가 0부터 시작
        page--;

        //페이지 조회 조건 (현재 페이지, 페이지당 글수, 정렬 순서, 정렬 기준 컬럼)
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "boardNum");

        Page<BoardEntity> entityPage = null;

        switch (searchType) {
            case "title" :
                entityPage = boardRepository.findByTitleContaining(searchWord, pageable);     break;
            case "contents" :
                entityPage = boardRepository.findByContentsContaining(searchWord, pageable);     break;
            case "id" :
                entityPage = boardRepository.findByMember_MemberId(searchWord, pageable);     break;
            default :
                entityPage = boardRepository.findAll(pageable);     break;
        }

        log.debug("조회된 결과 엔티티페이지 : {}", entityPage.getContent());

        //entityPage의 각 요소들을 순회하면서 convertToDTO() 메소드로 전달하여 DTO로 변환하고
        //이를 다시 새로운 Page객체로 만든다.
        Page<BoardDTO> boardDTOPage = entityPage.map(this::convertToDTO);
        return boardDTOPage;
    }

    /**
     * DB에서 조회한 게시글 정보인 BoardEntity 객체를 BoardDTO 객체로 변환
     * @param entity    게시글 정보 Entity 객체
     * @return          게시글 정보 DTO 개체
     */
    private BoardDTO convertToDTO(BoardEntity entity) {
        return BoardDTO.builder()
            .boardNum(entity.getBoardNum())
            .memberId(entity.getMember() != null ? entity.getMember().getMemberId() : null)
            .memberName(entity.getMember() != null ? entity.getMember().getMemberName() : null)
            .title(entity.getTitle())
            .contents(entity.getContents())
            .viewCount(entity.getViewCount())
            .likeCount(entity.getLikeCount())
            .originalName(entity.getOriginalName())
            .fileName(entity.getFileName())
            .createDate(entity.getCreateDate())
            .updateDate(entity.getUpdateDate())
            .build();
    }

    /**
     * ReplyEntity객체를 ReplyDTO 객체로 변환
     * @param entity    리플 정보 Entity 객체
     * @return          리플 정보 DTO 객체
     */
    private ReplyDTO convertToReplyDTO(ReplyEntity entity) {
        return ReplyDTO.builder()
                .replyNum(entity.getReplyNum())
                .boardNum(entity.getBoard().getBoardNum())
                .memberId(entity.getMember().getMemberId())
                .memberName(entity.getMember().getMemberName())
                .contents(entity.getContents())
                .createDate(entity.getCreateDate())
                .build();
    }

    /**
     * 게시글 1개 조회
     * @param boardNum          글번호
     * @return the BoardDTO     글 정보
     * @throws EntityNotFoundException 게시글이 없을 때 예외
     */
    public BoardDTO getBoard(int boardNum) {
        BoardEntity entity = boardRepository.findById(boardNum)
                .orElseThrow(() -> new EntityNotFoundException("해당 번호의 글이 없습니다."));

        entity.setViewCount(entity.getViewCount() + 1);
        log.debug("{}번 게시물 조회 결과 : {}", boardNum, entity);

        BoardDTO dto = convertToDTO(entity);

        List<ReplyDTO> replyDTOList = new ArrayList<ReplyDTO>();
        for (ReplyEntity replyEntity : entity.getReplyList()) {
            ReplyDTO replyDTO = convertToReplyDTO(replyEntity);
            replyDTOList.add(replyDTO);
        }
        dto.setReplyList(replyDTOList);
        return dto;
    }

    /**
     * 게시글 삭제
     * @param boardNum  삭제할 글번호
     * @param username  로그인한 아이디
     * @param uploadPath 첨부파일이 저장된 경로
     */
    public void delete(int boardNum, String username, String uploadPath) {
        BoardEntity boardEntity = boardRepository.findById(boardNum)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));

        if (!boardEntity.getMember().getMemberId().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        
        //boardEntity로 파일 유무 확인 가능
        //첨부파일이 있으면 삭제
        if(boardEntity.getFileName() != null && !boardEntity.getFileName().isEmpty()) {
        	File file = new File(uploadPath, boardEntity.getFileName());
        	file.delete();
        }
        boardRepository.delete(boardEntity);
    }
    
    /**
     * 게시글 수정
     * @param boardDTO      수정할 글정보
     * @param username      로그인한 아이디
     */
    public void update(BoardDTO boardDTO, String username, String uploadPath, MultipartFile upload) {
        BoardEntity boardEntity = boardRepository.findById(boardDTO.getBoardNum())
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));

        if (!boardEntity.getMember().getMemberId().equals(username)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 기존 파일 삭제
        if (boardEntity.getFileName() != null && !boardEntity.getFileName().isEmpty()) {
            File File = new File(uploadPath, boardEntity.getFileName());
            if (File.exists()) {
                File.delete();
            }
        }

        // 새로운 파일 업로드
        if (upload != null && !upload.isEmpty()) {
            String originalName = upload.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String uuidString = UUID.randomUUID().toString();
            String fileName = dateString + "_" + uuidString + extension;

            try {
                File filePath = new File(uploadPath + "/" + fileName);
                upload.transferTo(filePath);

                boardEntity.setOriginalName(originalName);
                boardEntity.setFileName(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 새로운 파일이 없을 때 기존 파일 정보를 유지
            boardEntity.setOriginalName(boardEntity.getOriginalName());
            boardEntity.setFileName(boardEntity.getFileName());
        }

        // 전달된 정보 수정
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContents(boardDTO.getContents());
    }


    /**
     * 리플 저장
     * @param replyDTO 작성한 리플 정보
     * @throws EntityNotFoundException 사용자 정보가 없을 때 예외
     */
    public void replyWrite(ReplyDTO replyDTO) {
        MemberEntity memberEntity = memberRepository.findById(replyDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("사용자 아이디가 없습니다."));

        BoardEntity boardEntity = boardRepository.findById(replyDTO.getBoardNum())
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));

        ReplyEntity entity = ReplyEntity.builder()
                .board(boardEntity)
                .member(memberEntity)
                .contents(replyDTO.getContents())
                .build();

        replyRepository.save(entity);
    }


    /**
     * 리플 삭제
     * @param replyNum  삭제할 리플 번호
     * @param username  로그인한 아이디
     */
    public void replyDelete(Integer replyNum, String username) {
        ReplyEntity replyEntity = replyRepository.findById(replyNum)
                .orElseThrow(() -> new EntityNotFoundException("리플이 없습니다."));

        if (!replyEntity.getMember().getMemberId().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        replyRepository.delete(replyEntity);
    }

	public void download(Integer boardNum, String uploadPath, HttpServletResponse response) {
		//전달된 글 번호로 글 정보 조회
		BoardEntity boardEntity = boardRepository.findById(boardNum)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));
		
		//in(글자, byte), out(글, byte)
	
		//원래의 파일명을 헤더 정보에 세팅
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" 
					+ URLEncoder.encode(boardEntity.getOriginalName(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//저장된 파일 경로
		String fullPath = uploadPath + "/" + boardEntity.getFileName();
		
		//서버의 파일을 읽을 입력 스트림과 클라이언트에게 전달할 출력 스트림
		FileInputStream filein = null;
		ServletOutputStream fileout = null;
		
		try {
			filein = new FileInputStream(fullPath); //서버의 파일과 프로그램
			fileout = response.getOutputStream(); //프로그램과 클라이언트
			
			//Spring의 파일 관련 유틸 이용하여 출력
			FileCopyUtils.copy(filein, fileout);
			
			//close하기 전까지 계속 전송 stream객체를 닫음
			filein.close();
			fileout.close();
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}


	public void like(LikesDTO likesDTO) {
		// TODO Auto-generated method stub
		 MemberEntity memberEntity = memberRepository.findById(likesDTO.getMemberId())
	                .orElseThrow(() -> new EntityNotFoundException("사용자 아이디가 없습니다."));

	        BoardEntity boardEntity = boardRepository.findById(likesDTO.getBoardNum())
	                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다."));

	        //이미 좋아요를 누른 적이 있으면 좋아요를 요청할 수 없도록
	        if (likesRepository.findByMemberAndBoard(memberEntity, boardEntity).isPresent()) {
	        	throw new EntityNotFoundException("좋아요를 할 수 없습니다.");
	        }
	        
	        LikesEntity entity = LikesEntity.builder()
	                .board(boardEntity)
	                .member(memberEntity)
	                .build();
	        //좋아요 테이블에 좋아요 저장
	        likesRepository.save(entity);
	        
	        //게시물의 좋아요 카운트 +1
	        boardEntity.setLikeCount(boardEntity.getLikeCount() + 1);
	        boardRepository.save(boardEntity);
		
		
	}
	
	
	
	
}
