package artxew.framework.layers.img.svc;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.layers.img.dto.req.CreateOfModify_Img_ReqDto;
import artxew.framework.layers.img.dto.req.Move_Img_ReqDto;
import artxew.framework.layers.img.dto.res.Query_Img_ResDto;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@Service
public class ImgSvc extends BaseService {

	@Value("${artxew.file.image}")
	private String imageFilePath;

	private final CommonDao commonDao;
	private final Pattern safePathRegex = Pattern.compile("^[^/.]+$");

	/**
	 * @author Artxe2
	 */
	@Transactional
	public Query_Img_ResDto createOfModify_ImgPath(CreateOfModify_Img_ReqDto reqDto) {
		Query_Img_ResDto resDto = query_Img(reqDto);
		if (resDto == null) {
			create_Img(reqDto);
			resDto = new Query_Img_ResDto();
		} else if(!reqDto.getExts().equals(resDto.getExts())) {
			modify_Img(reqDto);
		}
		resDto.setClCd(reqDto.getClCd());
		resDto.setRefKey(reqDto.getRefKey());
		resDto.setExts(reqDto.getExts());
		resDto.setAlt(reqDto.getAlt());
		return resDto;
	}

	/**
	 * @author Artxe2
	 */
	private int create_Img(CreateOfModify_Img_ReqDto reqDto) {
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	private int modify_Img(CreateOfModify_Img_ReqDto reqDto) {
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		return 1;
	}

	/**
	 * @author Artxe2
	 */
	@Transactional
	public int move_Img(Move_Img_ReqDto reqDto) {
		if (
			!safePathRegex.matcher(reqDto.getMoveKey()).find()
			|| reqDto.getMoveKey().length() > 40
		) {
			exception("bad-request");
		}
		Query_Img_ResDto resDto = query_Img(reqDto);
		if (resDto == null) {
			throw exception("not-found");
		}
		if (commonDao.update(reqDto) != 1) {
			exception("conflict");
		}
		try {
			Process process = new ProcessBuilder(
				"mv"
				, new StringBuilder(imageFilePath)
					.append('/')
					.append(resDto.getClCd())
					.append('/')
					.append(resDto.getRefKey())
					.append('.')
					.append(resDto.getExts())
					.toString()
				, new StringBuilder(imageFilePath)
					.append('/')
					.append(resDto.getClCd())
					.append('/')
					.append(reqDto.getMoveKey())
					.append('.')
					.append(resDto.getExts())
					.toString()
			).start();
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				exception(exitCode, "internal-error");
			}
			return 1;
		} catch (DefinedException e) {
			throw e;
	 	} catch (Exception e) {
			throw exception("not-found", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public <T> Query_Img_ResDto query_Img(T reqDto) {
		return commonDao.select(reqDto);
	}
}