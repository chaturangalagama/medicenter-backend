package com.ilt.cms.api.container;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.HttpApiResponse;

/**
 * <p>
 * <code>{@link PageableHttpApiResponse}</code> -
 * A container for the HTTP response where we may include pagination information as well.
 * </p>
 *
 * @author prabath.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageableHttpApiResponse extends HttpApiResponse {

    private int totalPages;
    private int pageNumber;
    private int totalElements;

    public PageableHttpApiResponse(int totalElements, int totalPage, int pageNumber, StatusCode status) {
        super(status);
        this.totalPages = totalPage;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
    }

    public PageableHttpApiResponse(int totalElements, int totalPage, int pageNumber, String message) {
        super(message);
        this.totalPages = totalPage;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
    }

    public PageableHttpApiResponse(int totalElements, int totalPage, int pageNumber, Object payload) {
        super(payload);
        this.totalPages = totalPage;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
    }

    public PageableHttpApiResponse(int totalElements, int totalPage, int pageNumber, StatusCode statusCode, String message) {
        super(statusCode, message);
        this.totalPages = totalPage;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
    }

    public PageableHttpApiResponse(int totalElements, int totalPage, int pageNumber, StatusCode statusCode, String message, Throwable ex) {
        super(statusCode, message, ex);
        this.totalPages = totalPage;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalElements() {
        return totalElements;
    }
}
