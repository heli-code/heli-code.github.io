package com.musicflow.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 数据库查询优化工具类
 */
@Slf4j
public class QueryOptimizer {

    /**
     * 分页查询优化 - 避免N+1查询问题
     */
    public static <T> Page<T> optimizedFindAll(JpaSpecificationExecutor<T> repository, 
                                              Specification<T> spec, 
                                              Pageable pageable) {
        // 优化分页参数
        Pageable optimizedPageable = optimizePageable(pageable);
        
        // 执行查询
        Page<T> result = repository.findAll(spec, optimizedPageable);
        
        // 记录查询性能
        log.debug("分页查询完成 - 总数: {}, 当前页: {}, 页大小: {}", 
                 result.getTotalElements(), result.getNumber(), result.getSize());
        
        return result;
    }

    /**
     * 优化分页参数
     */
    private static Pageable optimizePageable(Pageable pageable) {
        // 限制最大页大小，避免一次性查询过多数据
        int pageSize = Math.min(pageable.getPageSize(), 100);
        
        // 优化排序，避免复杂排序导致的性能问题
        Sort optimizedSort = optimizeSort(pageable.getSort());
        
        return org.springframework.data.domain.PageRequest.of(
            pageable.getPageNumber(), 
            pageSize, 
            optimizedSort
        );
    }

    /**
     * 优化排序条件
     */
    private static Sort optimizeSort(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            // 默认按创建时间倒序排序
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        
        // 限制排序字段数量，避免性能问题
        List<Sort.Order> orders = new ArrayList<>();
        int count = 0;
        for (Sort.Order order : sort) {
            if (count < 3) { // 最多支持3个排序字段
                orders.add(order);
                count++;
            }
        }
        
        return Sort.by(orders);
    }

    /**
     * 构建搜索条件规范
     */
    public static <T> Specification<T> buildSearchSpecification(String keyword, String... fields) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            
            String searchTerm = "%" + keyword.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            
            for (String field : fields) {
                predicates.add(cb.like(cb.lower(root.get(field)), searchTerm));
            }
            
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 构建时间范围查询规范
     */
    public static <T> Specification<T> buildDateRangeSpecification(String dateField, 
                                                                  java.time.LocalDateTime startDate, 
                                                                  java.time.LocalDateTime endDate) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(dateField), startDate));
            }
            
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(dateField), endDate));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 批量查询优化 - 使用IN查询避免多次单条查询
     */
    public static <T, ID> List<T> batchFindByIds(JpaSpecificationExecutor<T> repository, 
                                                 List<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 分批处理，避免IN查询参数过多
        List<T> results = new ArrayList<>();
        int batchSize = 1000; // 每批最多1000个ID
        
        for (int i = 0; i < ids.size(); i += batchSize) {
            List<ID> batchIds = ids.subList(i, Math.min(i + batchSize, ids.size()));
            
            Specification<T> spec = (root, query, cb) -> 
                root.get("id").in(batchIds);
            
            List<T> batchResults = repository.findAll(spec);
            results.addAll(batchResults);
        }
        
        log.debug("批量查询完成 - ID数量: {}, 结果数量: {}", ids.size(), results.size());
        return results;
    }

    /**
     * 查询性能监控
     */
    public static <T> T monitorQuery(String queryName, QuerySupplier<T> supplier) {
        long startTime = System.currentTimeMillis();
        
        try {
            T result = supplier.get();
            long duration = System.currentTimeMillis() - startTime;
            
            if (duration > 1000) { // 超过1秒的查询记录警告
                log.warn("慢查询检测 - {}: {}ms", queryName, duration);
            } else {
                log.debug("查询完成 - {}: {}ms", queryName, duration);
            }
            
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("查询失败 - {}: {}ms, 错误: {}", queryName, duration, e.getMessage());
            throw e;
        }
    }

    /**
     * 查询供应商接口
     */
    @FunctionalInterface
    public interface QuerySupplier<T> {
        T get();
    }

    /**
     * 构建关联查询优化规范
     */
    public static <T> Specification<T> buildJoinFetchSpecification(String... fetchPaths) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // 设置DISTINCT避免重复数据
            query.distinct(true);
            
            // 预加载关联实体，避免N+1查询
            for (String path : fetchPaths) {
                root.fetch(path);
            }
            
            return cb.conjunction();
        };
    }

    /**
     * 统计查询优化
     */
    public static <T> Long optimizedCount(JpaSpecificationExecutor<T> repository, 
                                         Specification<T> spec) {
        return monitorQuery("统计查询", () -> repository.count(spec));
    }
}