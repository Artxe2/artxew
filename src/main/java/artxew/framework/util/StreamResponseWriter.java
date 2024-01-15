package artxew.framework.util;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StreamUtils;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.environment.flowlog.FlowLogHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import net.sf.jxls.transformer.XLSTransformer;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author Artxe2
 */
public final class StreamResponseWriter {

	/**
	 * @author Artxe2
	 */
	@Data
	public static class FileDto {
		private String filePath;
		private String fileName;
	}
	protected static String uploadFilePath;

	/**
	 * @author Artxe2
	 */
	private StreamResponseWriter() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	@SuppressWarnings("null")
	public static void file(
		String filePath
		, String fileName
	) {
		StringBuilder sb = new StringBuilder("StreamResponseWriter.file(")
				.append(filePath)
				.append(", ")
				.append(fileName)
				.append(')');
		FlowLogHolder.touch(sb.toString());
		File file = new File(uploadFilePath, filePath.replace("..", ""));
		HttpServletResponse response = SessionHelper.response();
		if (fileName == null) {
			fileName = file.getName();
		} else {
			fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
					.replace("\\+", "%20");
		}
		setAttachment(response, fileName, "application/octet-stream; charset=UTF-8");
		try (
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
		) {
			response.setContentLengthLong(file.length());
			StreamUtils.copy(bis, response.getOutputStream());
		} catch (Exception e) {
			response.reset();
			throw new DefinedException("stream-file-error", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public static void zip(
		String fileName
		, List<FileDto> fileList
	) {
		StringBuilder sb = new StringBuilder("StreamResponseWriter.zip(")
				.append(fileName)
				.append(", ")
				.append(fileList.size())
				.append(" files)");
		FlowLogHolder.touch(sb.toString());
		HttpServletResponse response = SessionHelper.response();
		fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
				.replace("\\+", "%20");
		setAttachment(response, fileName, "application/octet-stream; charset=UTF-8");
		try {
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
			long length = 0;
			for (var file : fileList) {
				FileSystemResource resource = new FileSystemResource(new File(uploadFilePath, file.filePath.replace("..", "")));
				fileName = file.getFileName();
				if (fileName == null) {
					fileName = resource.getFilename();
				}
				ZipEntry e = new ZipEntry(fileName);
				e.setSize(resource.contentLength());
				e.setTime(System.currentTimeMillis());
				length += e.getSize();
				zos.putNextEntry(e);
				StreamUtils.copy(resource.getInputStream(), zos);
				zos.closeEntry();
			}
			response.setContentLengthLong(length);
			zos.finish();
		} catch (Exception e) {
			response.reset();
			throw new DefinedException("stream-zip-error", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	@SuppressWarnings("null")
	public static void excel(
		Map<String, Object> model
		, String template
		, String fileName
	) {
		StringBuilder sb = new StringBuilder("StreamResponseWriter.excel(")
				.append(template)
				.append(", ")
				.append(fileName)
				.append(')');
		FlowLogHolder.touch(sb.toString());
		if (fileName == null) {
			fileName = new File(template).getName();
		} else {
			fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
					.replace("\\+", "%20");
		}
		HttpServletResponse response = SessionHelper.response();
		setAttachment(response, fileName, "ms-vnd/excel; charset=UTF-8");
		try (
			InputStream is = new BufferedInputStream(
				new ClassPathResource(template.replace("..", "")).getInputStream()
			);
			BufferedInputStream bis = new BufferedInputStream(is);
		) {
			XLSTransformer xls = new XLSTransformer();
			Workbook excel = xls.transformXLS(bis, model);
			excel.write(response.getOutputStream());
		} catch (Exception e) {
			response.reset();
			throw new DefinedException("stream-excel-error", e);
		}
	}

	/**
	 * @author Artxe2
	 */
	private static void setAttachment(HttpServletResponse response, String fileName, String contentType) {
		StringBuilder sb = new StringBuilder("attachment; filename=\"")
				.append(new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
				.append('"');
		response.reset();
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", sb.toString());
	}
}