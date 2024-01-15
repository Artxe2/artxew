package artxew.framework.layers.img.ctrl;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.layers.img.dto.req.CreateOfModify_Img_ReqDto;
import artxew.framework.layers.img.dto.res.Query_Img_ResDto;
import artxew.framework.layers.img.svc.ImgSvc;
import artxew.framework.util.SessionHelper;
import artxew.framework.util.StringUtil;
import artxew.project.enums.StringYN;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequestMapping("api/img")
@RequiredArgsConstructor
@RestController
@Tag(name = "이미지 관리")
public class ImgCtrl extends BaseController {

	@Value("${artxew.image-exts}")
	private String[] imageExts;

	@Value("${artxew.file.image}")
	private String imageFilePath;

	private final ImgSvc imgSvc;
	private final Pattern safePathRegex = Pattern.compile("^[^/.]+$");

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "이미지 업로드")
	@PostMapping(path = "{imgClCd}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ServerResponseDto<
	CreateOfModify_Img_ReqDto
	>> uploadImage(
		@PathVariable("imgClCd")
		String clCd
		, @RequestParam(name = "refKey", required = false)
		String refKey
		, @RequestParam(name = "useDirYn", required = false)
		StringYN useDirYn
		, @RequestPart("img")
		MultipartFile image
	) throws IOException {
		if (!safePathRegex.matcher(clCd).find()) {
			exception("bad-request");
		}
		if (refKey == null || refKey.isEmpty()) {
			refKey = StringUtil.uniqueKey(20);
		} else if (
			!safePathRegex.matcher(refKey).find()
			|| refKey.length() > 40
		) {
			exception("bad-request");
		} else if (useDirYn != null && useDirYn == StringYN.Y) {
			if (refKey.length() > 19) {
				exception("bad-request");
			}
			refKey = new StringBuilder(refKey)
				.append('/')
				.append(StringUtil.uniqueKey(20))
				.toString();
		}
		String fileName = image.getOriginalFilename();
		String exts = fileName == null
			? null
			: fileName.substring(fileName.lastIndexOf('.') + 1);
		Integer.valueOf(clCd);
		for (String allow : imageExts) {
			if (allow.equals(exts)) {
				String localFileName = new StringBuilder(clCd)
					.append('/')
					.append(refKey)
					.append('.')
					.append(exts)
					.toString();
				File localFile = new File(
					imageFilePath
					, localFileName
				);
				localFile.mkdirs();
				image.transferTo(localFile);
				CreateOfModify_Img_ReqDto reqDto = new CreateOfModify_Img_ReqDto();
				reqDto.setClCd(clCd);
				reqDto.setRefKey(refKey);
				reqDto.setExts(exts);
				reqDto.setAlt(image.getOriginalFilename());
				imgSvc.createOfModify_ImgPath(reqDto);
				return processResult(reqDto);
			}
		}
		throw exception("bad-request", fileName);
	}

	/**
	 * @author Artxe2
	 */
	@GetMapping("{imgClCd}/{refKey}")
	@Operation(summary = "이미지 조회")
	@SuppressWarnings("null")
	public void queryImage(
		@PathVariable("imgClCd")
		String imgClCd
		, @PathVariable("refKey")
		String refKey
	) {
		CreateOfModify_Img_ReqDto reqDto = new CreateOfModify_Img_ReqDto();
		reqDto.setClCd(imgClCd);
		reqDto.setRefKey(refKey);
		Query_Img_ResDto resDto = imgSvc.query_Img(reqDto);
		if (resDto == null) {
			exception("not-found");
		}
		String localFileName = new StringBuilder(imgClCd)
			.append('/')
			.append(refKey)
			.append('.')
			.append(resDto.getExts())
			.toString();
		File file = new File(imageFilePath, localFileName);
		HttpServletResponse response = SessionHelper.response();
		setInline(response, new StringBuilder("image").append('/').append(resDto.getExts()).toString());
		try (
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
		) {
			response.setContentLengthLong(file.length());
			response.setHeader("Cache-Control", "max-age=60840");
			StreamUtils.copy(bis, response.getOutputStream());
		} catch (Exception e) {
			response.reset();
			throw new DefinedException("stream-resource-error", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	@GetMapping("{imgClCd}/{refDir}/{refKey}")
	@Operation(summary = "폴더 이미지 조회")
	public void queryImage(
		@PathVariable("imgClCd")
		String imgClCd
		, @PathVariable("refDir")
		String refDir
		, @PathVariable("refKey")
		String refKey
	) {
		queryImage(
			imgClCd
			, new StringBuilder(refDir)
				.append('/')
				.append(refKey)
				.toString()
		);
	}

	/**
	 * @author Artxe2
	 */
	private static void setInline(HttpServletResponse response, String contentType) {
		response.reset();
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "inline");
	}
}