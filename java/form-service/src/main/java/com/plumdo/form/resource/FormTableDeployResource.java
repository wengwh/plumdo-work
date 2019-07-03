package com.plumdo.form.resource;

import com.plumdo.common.client.jdbc.JdbcClient;
import com.plumdo.common.resource.BaseResource;
import com.plumdo.form.constant.TableConstant;
import com.plumdo.form.domain.*;
import com.plumdo.form.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据表部署资源类
 *
 * @author wengwh
 * @date 2018年8月29日
 */
@RestController
public class FormTableDeployResource extends BaseResource {
    private final FormTableRepository formTableRepository;
    private final FormFieldRepository formFieldRepository;
    private final FormLayoutRepository formLayoutRepository;
    private final ByteArrayRepository byteArrayRepository;
    private final FormDefinitionRepository formDefinitionRepository;
    private final JdbcClient jdbcClient;

    @Autowired
    public FormTableDeployResource(FormTableRepository formTableRepository, FormFieldRepository formFieldRepository, FormLayoutRepository formLayoutRepository, ByteArrayRepository byteArrayRepository, FormDefinitionRepository formDefinitionRepository, JdbcClient jdbcClient) {
        this.formTableRepository = formTableRepository;
        this.formFieldRepository = formFieldRepository;
        this.formLayoutRepository = formLayoutRepository;
        this.byteArrayRepository = byteArrayRepository;
        this.formDefinitionRepository = formDefinitionRepository;
        this.jdbcClient = jdbcClient;
    }

    @PostMapping("/form-tables/{id}/deploy")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deployFormTable(@PathVariable Integer id) {
        FormTable formTable = formTableRepository.findOne(id);
        List<FormField> formFields = formFieldRepository.findByTableId(id);
        List<FormLayout> formLayouts = formLayoutRepository.findByTableId(id);

        String formKey = formTable.getKey();
        String relationTable = TableConstant.RELATION_TABLE_PRE + formKey;
        int version;
        FormDefinition formDefinition = formDefinitionRepository.findLatestFormDefinitionByKey(formKey);
        if (formDefinition == null) {
            createDataTable(relationTable, formFields);
            version = 1;
        } else {
            alterDataTable(relationTable, formFields);
            version = formDefinition.getVersion() + 1;
        }

        formDefinition = new FormDefinition();
        formDefinition.setName(formTable.getName());
        formDefinition.setVersion(version);
        formDefinition.setKey(formKey);
        formDefinition.setRelationTable(relationTable);
        formDefinition.setCategory(formTable.getCategory());
        formDefinition.setTenantId(formTable.getTenantId());

        try {
            ByteArray byteArray = new ByteArray();
            byteArray.setName(formTable.getName() + TableConstant.TABLE_FIELD_SUFFIX);
            byteArray.setContentByte(objectMapper.writeValueAsBytes(formFields));
            byteArrayRepository.save(byteArray);
            formDefinition.setFieldSourceId(byteArray.getId());

            //复制一遍布局的设计存储，防止表单设计修改之后，定义也跟随编号
            copyFormLayouts(formLayouts);

            byteArray = new ByteArray();
            byteArray.setName(formTable.getName() + TableConstant.TABLE_LAYOUT_SUFFIX);
            byteArray.setContentByte(objectMapper.writeValueAsBytes(formLayouts));
            byteArrayRepository.save(byteArray);
            formDefinition.setLayoutSourceId(byteArray.getId());

            formDefinitionRepository.save(formDefinition);
        } catch (Exception e) {
            logger.error("deploy form exception", e);
        }
    }

    private void copyFormLayouts(List<FormLayout> formLayouts) {
        for (FormLayout formLayout : formLayouts) {
            ByteArray byteArray = byteArrayRepository.findOne(formLayout.getEditorSourceId());
            if (byteArray == null) {
                continue;
            }
            ByteArray copyByteArray = new ByteArray();
            copyByteArray.setName(byteArray.getName());
            copyByteArray.setContentByte(byteArray.getContentByte());
            byteArrayRepository.save(copyByteArray);

            formLayout.setEditorSourceId(copyByteArray.getId());
        }

    }

    private void createDataTable(String tableName, List<FormField> formFields) {
        StringBuilder sbSql = new StringBuilder().append("CREATE TABLE ").append(TableConstant.DATABASE_NAME).append(tableName)
                .append("( `id_` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID', ");
        for (FormField formField : formFields) {
            sbSql.append("`").append(formField.getKey()).append("` VARCHAR(512),");
        }
        sbSql.append("PRIMARY KEY ( id_ ))");

        jdbcClient.execute(sbSql.toString());
    }

    private void alterDataTable(String tableName, List<FormField> formFields) {
        String sql = "select column_name from information_schema.columns where table_name='" + tableName + "'";
        List<String> columns = jdbcClient.queryForList(sql).stream().map(map -> map.getAsString("column_name")).collect(Collectors.toList());

        for (FormField formField : formFields) {
            String key = formField.getKey();
            if (!columns.contains(key)) {
                String alterSql = "ALTER TABLE " + TableConstant.DATABASE_NAME + tableName + " ADD COLUMN `" + key + "` VARCHAR(512);";
                jdbcClient.execute(alterSql);
            }
        }
    }
}
