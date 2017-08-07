package mx.infotec.dads.datapoolgenerator.web.rest;

import com.codahale.metrics.annotation.Timed;

import mx.infotec.dads.datapoolgenerator.domain.DataPool;
import mx.infotec.dads.datapoolgenerator.domain.DataPoolRequest;
import mx.infotec.dads.datapoolgenerator.repository.DataPoolRepository;
import mx.infotec.dads.datapoolgenerator.service.DataPoolGeneratorService;
import mx.infotec.dads.datapoolgenerator.service.dto.DataPoolRequestDTO;
import mx.infotec.dads.datapoolgenerator.service.mapper.DataPoolRequestMapper;
import mx.infotec.dads.datapoolgenerator.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DataPool.
 */
@RestController
@RequestMapping("/api")
public class DataPoolResource {

    private final Logger log = LoggerFactory.getLogger(DataPoolResource.class);

    private static final String ENTITY_NAME = "dataPool";

    private final DataPoolRepository dataPoolRepository;
    
    private DataPoolGeneratorService dataPoolGenerator;

    public DataPoolResource(DataPoolRepository dataPoolRepository, DataPoolGeneratorService dataPoolGenerator) {
        this.dataPoolRepository = dataPoolRepository;
        this.dataPoolGenerator = dataPoolGenerator;
    }
    
    /**
     * POST  /data-pools/generate : Generate a new dataPool from a request.
     *
     * @param requestDTO the requestDTO that generate a new dataPool
     * @return the ResponseEntity with status 201 (Created) and with body the new dataPool, or with status 400 (Bad Request) if the requestDTO has errors
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-pools/generate")
    @Timed
    public ResponseEntity<DataPool> generateDataPool(@RequestBody DataPoolRequestDTO requestDTO) throws URISyntaxException {
        log.debug("REST request to generate DataPool : {}", requestDTO);
        DataPoolRequest request = DataPoolRequestMapper.toEntity(requestDTO);
		DataPool result = dataPoolGenerator.generate(request);
        return createDataPool(result);
    }

    /**
     * POST  /data-pools : Create a new dataPool.
     *
     * @param dataPool the dataPool to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataPool, or with status 400 (Bad Request) if the dataPool has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-pools")
    @Timed
    public ResponseEntity<DataPool> createDataPool(@RequestBody DataPool dataPool) throws URISyntaxException {
        log.debug("REST request to save DataPool : {}", dataPool);
        if (dataPool.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataPool cannot already have an ID")).body(null);
        }
        DataPool result = dataPoolRepository.save(dataPool);
        return ResponseEntity.created(new URI("/api/data-pools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-pools : Updates an existing dataPool.
     *
     * @param dataPool the dataPool to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataPool,
     * or with status 400 (Bad Request) if the dataPool is not valid,
     * or with status 500 (Internal Server Error) if the dataPool couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-pools")
    @Timed
    public ResponseEntity<DataPool> updateDataPool(@RequestBody DataPool dataPool) throws URISyntaxException {
        log.debug("REST request to update DataPool : {}", dataPool);
        if (dataPool.getId() == null) {
            return createDataPool(dataPool);
        }
        DataPool result = dataPoolRepository.save(dataPool);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataPool.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-pools : get all the dataPools.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataPools in body
     */
    @GetMapping("/data-pools")
    @Timed
    public List<DataPool> getAllDataPools() {
        log.debug("REST request to get all DataPools");
        return dataPoolRepository.findAll();
    }

    /**
     * GET  /data-pools/:id : get the "id" dataPool.
     *
     * @param id the id of the dataPool to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataPool, or with status 404 (Not Found)
     */
    @GetMapping("/data-pools/{id}")
    @Timed
    public ResponseEntity<DataPool> getDataPool(@PathVariable String id) {
        log.debug("REST request to get DataPool : {}", id);
        DataPool dataPool = dataPoolRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dataPool));
    }

    /**
     * DELETE  /data-pools/:id : delete the "id" dataPool.
     *
     * @param id the id of the dataPool to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-pools/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataPool(@PathVariable String id) {
        log.debug("REST request to delete DataPool : {}", id);
        dataPoolRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
