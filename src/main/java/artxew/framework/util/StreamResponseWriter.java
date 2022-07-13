package artxew.framework.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StreamUtils;

import artxew.framework.environment.exception.DefinedException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Slf4j
public final class StreamResponseWriter {

    @Data
    public static class FileDto {
        private String filePath;
        private String fileName;
    }

    private static final String ROOT_PATH = new File(
        "src/main/resources/public"
    ).getAbsolutePath();

    private StreamResponseWriter() {
        throw new IllegalStateException("Utility class");
    }

    public static void file(
        String filePath
        , String fileName
    ) {
        log.info("stream file({}, {})", filePath, fileName);

        HttpServletResponse response = SessionMap.response();
        File file = new File(ROOT_PATH, filePath);

        try (
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
        ) {
            if (fileName == null) {
                fileName = file.getName();
            } else {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                        .replace("\\+", "%20");
            }
            initResponse(response, "application/octet-stream", fileName);

            long length = file.length();
            String contentLength = String.valueOf(length);

            response.setHeader ("Content-Length", contentLength);
            StreamUtils.copy(bis, os);
        } catch (Exception e) {
            throw new DefinedException("stream-file-error", e);
        }
    }

    public static void zip(
        String fileName
        , List<FileDto> fileList
    ) {
        log.info("stream zip({}, {} files)", fileName, fileList.size());

        HttpServletResponse response = SessionMap.response();

        try (
            OutputStream os = response.getOutputStream();
            ZipOutputStream zos = new ZipOutputStream(os);
        ) {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replace("\\+", "%20");
            initResponse(response, "application/octet-stream", fileName);

            long length = 0;

            for (FileDto file : fileList) {
                File f = new File(ROOT_PATH, file.filePath);
                FileSystemResource resource = new FileSystemResource(f);

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

            String contentLength = String.valueOf(length);

            response.setHeader ("Content-Length", contentLength);
            zos.finish();
        } catch (Exception e) {
            throw new DefinedException("stream-zip-error", e);
        }
    }

    public static void excel(
        Map<String, Object> model
        , String template
        , String fileName
    ) {
        log.info("stream excel({}, {})", template, fileName);

        HttpServletResponse response = SessionMap.response();
        try (
            OutputStream os = response.getOutputStream();
            InputStream is = new ClassPathResource(template).getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
        ) {
            XLSTransformer xls = new XLSTransformer();
            Workbook excel = xls.transformXLS(bis, model);

            if (fileName == null) {
                fileName = new File(template).getName();
            } else {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                        .replace("\\+", "%20");
            }
            initResponse(response, "ms-vnd/excel", fileName);
            excel.write(os);
        } catch (Exception e) {
            throw new DefinedException("stream-excel-error", e);
        }
    }

    private static void initResponse(
        HttpServletResponse response
        , String contentType
        , String fileName
    ) {
        StringBuilder sb = new StringBuilder("attachment; filename=\"");
        sb.append(
            new String(
                fileName.getBytes(StandardCharsets.UTF_8)
                , StandardCharsets.ISO_8859_1
            )
        );
        sb.append('"');
        response.reset();
        response.setContentType(contentType + "; charset=UTF-8");
        response.setHeader(
            "Content-Disposition"
            , sb.toString()
        );
    }
}