package id.sajiin.sajiinservices.store.repository.query;

import id.sajiin.sajiinservices.shared.core.BaseEntityRequest;
import id.sajiin.sajiinservices.shared.specification.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 1. Static Join: Always join categories when querying shops using this request
@QueryConfig(
    distinct = true, // Important for OneToMany joins to avoid duplicate parent rows
    joins = {
        @Join(path = "categories", type = JoinType.LEFT, fetch = true)
    }
)
public class ShopEntityRequest extends BaseEntityRequest {

    @QueryField
    private Long id;

    @QueryField
    private Long userId;

    @OrGroup("search")
    @QueryField(operator = QueryOperator.LIKE, ignoreCase = true)
    private String name;

    @OrGroup("search")
    @QueryField(operator = QueryOperator.LIKE, ignoreCase = true)
    private String location;

    // 2. Filter by joined field: Filter shops that have a specific category name
    // The path "categories.name" will automatically use the join defined above or create a new one
    @QueryField(path = "categories.name", operator = QueryOperator.LIKE, ignoreCase = true)
    private String categoryName;

    // 3. Dynamic Join: Only join 'products' if this boolean is true
    // (Assuming Shop has a 'products' relationship, adding it to Shop class next)
    @JoinControl(path = "products", type = JoinType.LEFT, fetch = true)
    private Boolean withProducts;

    @QueryField(operator = QueryOperator.IN)
    private List<String> status;

    @QueryField(operator = QueryOperator.BETWEEN, path = "createdAt")
    private List<String> createdDateRange;

    @QueryField(operator = QueryOperator.GREATER_THAN_OR_EQUAL, path = "createdAt")
    private String createdAfter;

}
