package artxew.framework.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.environment.websocket.WsSessionGroupHolder;
import artxew.project.layers.chat.dto.ws.WsChatDto;
import jakarta.websocket.Session;

/**
 * @author Artxe2
 */
public class ChattingHelper {
	public static String ADMIN_CHAT_FILE_NAME = "admin";
	protected static String chatFilePath;

	/**
	 * @author Artxe2
	 */
	private ChattingHelper() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	public static void broadcast(String chatNo, String json) throws IOException {
		Set<Session> set = WsSessionGroupHolder.getGroups(chatNo);
		if (set != null) {
			for (var s : set) {
				s.getBasicRemote().sendText(json);
			}
		}
	}

	/**
	 * @author Artxe2
	 */
	public static String readChattingLog(String fileName) throws Exception {
		File file = new File(chatFilePath, fileName);
		if (!file.exists()) return "[]";
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fc = raf.getChannel();
		FileLock lock = null;
		for (int i = 0;;) {
			try {
				lock = fc.tryLock();
				break;
			} catch (Exception e) {
				if (++i == 100) {
					if (fc.isOpen()) fc.close();
					raf.close();
					throw new DefinedException("chat-file-error");
				}
				Thread.sleep(50);
			}
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
				Channels.newInputStream(fc)
			);
			int length;
			byte[] buffer = new byte[8192];
			StringBuilder sb = new StringBuilder("[");
			while ((length = bis.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, length));
			}
			if (sb.length() > 1) {
				sb.setLength(sb.length() - 1);
			}
			bis.close();
			return sb.append("]").toString();
		} finally {
			if (lock != null && lock.isValid()) lock.release();
			if (fc.isOpen()) fc.close();
			raf.close();
		}
	}

	/**
	 * @author Artxe2
	 */
	public static WsChatDto toggleBlock_Chat(String fileName, String key, Boolean block) throws FileNotFoundException, IOException, InterruptedException{
		File file = new File(chatFilePath, fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fc = raf.getChannel();
		FileLock lock = null;
		for (int i = 0;;) {
			try {
				lock = fc.tryLock();
				break;
			} catch (Exception e) {
				if (++i == 100) {
					if (fc.isOpen()) fc.close();
					raf.close();
					throw new DefinedException("chat-file-error");
				}
				Thread.sleep(50);
			}
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
				Channels.newInputStream(fc)
			);
			int length;
			byte[] buffer = new byte[8192];
			StringBuilder sb = new StringBuilder("[");
			while ((length = bis.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, length));
			}
			sb.setLength(sb.length() - 1);
			sb.append("]");
			WsChatDto[] chatLog = StringUtil.fromJson(sb.toString(), WsChatDto[].class);
			WsChatDto result = null;
			for (var dto : chatLog) {
				if (key.equals(dto.getKey())) {
					result = dto;
					dto.setBlck(
						block != null
							? block
							: dto.getBlck() != null && dto.getBlck()
								? false
								: true
					);
					break;
				}
			}
			if (result != null) {
				sb.setLength(0);
				sb.append(StringUtil.toJson(chatLog).substring(1));
				sb.setLength(sb.length() - 1);
				sb.append(',');
				byte[] json = sb.toString().getBytes(StandardCharsets.UTF_8);
				File bak = new File(chatFilePath, fileName + ".bak");
				FileOutputStream fos = new FileOutputStream(bak);
				fos.write(json);
				fos.close();
				fc.truncate(0);
				OutputStream os = Channels.newOutputStream(fc);
				os.write(json);
				os.close();
				bak.delete();
			}
			return result;
		} finally {
			if (lock != null && lock.isValid()) lock.release();
			if (fc.isOpen()) fc.close();
			raf.close();
		}
	}

	/**
	 * @author Artxe2
	 */
	public static void writeChatLog(String fileName, String json) throws Exception {
		File file = new File(chatFilePath, fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fc = raf.getChannel();
		FileLock lock = null;
		for (int i = 0;;) {
			try {
				lock = fc.tryLock();
				break;
			} catch (Exception e) {
				if (++i == 100) {
					if (fc.isOpen()) fc.close();
					raf.close();
					throw new DefinedException("chat-file-error");
				}
				Thread.sleep(50);
			}
		}
		try {
			fc.position(fc.size());
			StringBuilder sb = new StringBuilder(json).append(',');
			OutputStream os = Channels.newOutputStream(fc);
			os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
			os.close();
		} finally {
			if (lock != null && lock.isValid()) lock.release();
			if (fc.isOpen()) fc.close();
			raf.close();
		}
	}

	/**
	 * @author Artxe2
	 */
	public static WsChatDto[] trimChattingLog(String fileName, int leftSize) throws FileNotFoundException, IOException, InterruptedException{
		File file = new File(chatFilePath, fileName);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		FileChannel fc = raf.getChannel();
		FileLock lock = null;
		for (int i = 0;;) {
			try {
				lock = fc.tryLock();
				break;
			} catch (Exception e) {
				if (++i == 100) {
					if (fc.isOpen()) fc.close();
					raf.close();
					throw new DefinedException("chat-file-error");
				}
				Thread.sleep(50);
			}
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
				Channels.newInputStream(fc)
			);
			int length;
			byte[] buffer = new byte[8192];
			StringBuilder sb = new StringBuilder("[");
			while ((length = bis.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, length));
			}
			sb.setLength(sb.length() - 1);
			sb.append("]");
			WsChatDto[] chatLog = StringUtil.fromJson(sb.toString(), WsChatDto[].class);
			if (chatLog.length > leftSize) {
				chatLog = Arrays.copyOfRange(chatLog, chatLog.length - leftSize, chatLog.length);
			}
			sb.setLength(0);
			sb.append(StringUtil.toJson(chatLog).substring(1));
			sb.setLength(sb.length() - 1);
			sb.append(',');
			byte[] json = sb.toString().getBytes(StandardCharsets.UTF_8);
			File bak = new File(chatFilePath, fileName + ".bak");
			FileOutputStream fos = new FileOutputStream(bak);
			fos.write(json);
			fos.close();
			fc.truncate(0);
			OutputStream os = Channels.newOutputStream(fc);
			os.write(json);
			os.close();
			bak.delete();
			return chatLog;
		} finally {
			if (lock != null && lock.isValid()) lock.release();
			if (fc.isOpen()) fc.close();
			raf.close();
		}
	}
}