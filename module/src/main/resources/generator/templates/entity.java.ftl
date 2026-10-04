package ${package.Entity};

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
<#list entityImports as type>
import ${type};
</#list>

/**
 * ${table.comment!entity}
 *
 * @author ${author}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ${entity} {
<#list table.fields as field>

    /** ${field.comment!field.propertyName} */
    private ${field.propertyType} ${field.propertyName};
</#list>
}
