package artxew.example.layers.es.ctrl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.elastic.CommonRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@ConditionalOnProperty("artxew.use.elastic")
@Profile({ "dev", "local" })
@RequestMapping("api/example/es")
@RequiredArgsConstructor
@RestController
@Tag(name = "Elastic search example")
public class ElasticSearchCtrl extends BaseController {

	private final CommonRepository commonRepository;

	/**
	 * @author Artxe2
	 */
	@GetMapping("select/{index}/{id}")
	public ResponseEntity<ServerResponseDto<
	Object
	>> esSelect(
		@PathVariable(name = "index", required = true)
		String index
		, @PathVariable(name = "id", required = true)
		String id
	) {
		Object resDto = commonRepository.select(index, id, Object.class);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@PostMapping("insert/{index}")
	public ResponseEntity<ServerResponseDto<
	Object
	>> esInsert(
		@PathVariable(name = "index", required = true)
		String index
		, @RequestBody(required = true) Object data
	) {
		Object resDto = commonRepository.insert(index, data);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@PostMapping("insert/{index}/{id}")
	public ResponseEntity<ServerResponseDto<
	Object
	>> esInsert(
		@PathVariable(name = "index", required = true)
		String index
		, @PathVariable(name = "id", required = true)
		String id
		, @RequestBody(required = true)
		Object data
	) {
		Object resDto = commonRepository.insert(index, id, data);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@PutMapping("merge/{index}/{id}")
	public ResponseEntity<ServerResponseDto<
	Object
	>> esMerge(
		@PathVariable(name = "index", required = true)
		String index
		, @PathVariable(name = "id", required = true)
		String id
		, @RequestBody(required = true)
		Object data
	) {
		Object resDto = commonRepository.merge(index, id, data);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@PatchMapping("update/{index}/{id}")
	public ResponseEntity<ServerResponseDto<
	Object
	>> esUpdate(
		@PathVariable(name = "index", required = true)
		String index
		, @PathVariable(name = "id", required = true)
		String id
		, @RequestBody(required = true)
		Object data
	) {
		Object resDto = commonRepository.update(index, id, data);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@DeleteMapping("delete/{index}/{id}")
	public ResponseEntity<ServerResponseDto<
	Object
	>> esDelete(
		@PathVariable(name = "index", required = true)
		String index
		, @PathVariable(name = "id", required = true)
		String id
	) {
		Object resDto = commonRepository.delete(index, id);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@GetMapping("search")
	public ResponseEntity<ServerResponseDto<
	Object
	>> esSearch(
		@Parameter(example = "{\"query\":{\"match_all\":{}}}") @RequestParam(name = "query")
		String query
	) {
		Object resDto = commonRepository.search(query);
		return processResult(resDto);
	}
}
