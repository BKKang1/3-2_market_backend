package creative.market.repository.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import creative.market.domain.product.Product;
import creative.market.domain.product.ProductImageType;
import creative.market.domain.product.QProduct;
import creative.market.domain.product.QProductImage;
import creative.market.repository.dto.ProductSigSrcAndIdRes;
import creative.market.repository.dto.QProductSigSrcAndIdRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static creative.market.domain.category.QItem.item;
import static creative.market.domain.product.QProduct.*;
import static creative.market.domain.product.QProductImage.*;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ProductSigSrcAndIdRes> findProductSigImgAndIdByLatestCreatedDate(int limit) { // 메인 페이지 전체 상품 최근 등록 순
        return queryFactory.select(new QProductSigSrcAndIdRes(productImage.path, product.id))
                .from(productImage)
                .join(productImage.product, product)
                .where(productImage.type.eq(ProductImageType.SIGNATURE))
                .orderBy(product.createdDate.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression imageTypeEq(ProductImageType productImageType) {
        return productImageType != null ? productImage.type.eq(productImageType) : null;
    }

    private OrderSpecifier<?> orderByFieldList(List<Long> productIds) {
        return Expressions.stringTemplate("FIELD({0}, {1})", product.id, productIds)
                .asc();
    }
}
