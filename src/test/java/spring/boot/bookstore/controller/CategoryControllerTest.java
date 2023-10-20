package spring.boot.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.boot.bookstore.dto.book.BookWithoutCategoryIdsDto;
import spring.boot.bookstore.dto.category.CategoryRequestDto;
import spring.boot.bookstore.dto.category.CategoryResponseDto;
import spring.boot.bookstore.service.book.BookService;
import spring.boot.bookstore.service.category.CategoryService;

@ContextConfiguration(classes = {CategoryController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @MockBean
    private BookService bookService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryController categoryController;
    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("Create category using valid input")
    public void createCategory_WithValidInput_ShouldReturnExpectedResult() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fiction");
        requestDto.setDescription("literature created from the imagination,"
                + " not presented as fact, though it may be based on a true story or situation.");
        when(categoryService.save(any(CategoryRequestDto.class))).thenAnswer(invocation -> {
            CategoryRequestDto dto = invocation.getArgument(0);
            CategoryResponseDto response = new CategoryResponseDto();
            response.setId(4L);
            response.setName(dto.getName());
            response.setDescription(dto.getDescription());
            return response;
        });
        CategoryResponseDto expectedCategoryResponse =
                categoryController.createCategory(requestDto);
        Assertions.assertNotNull(expectedCategoryResponse);
        assertEquals(4L, expectedCategoryResponse.getId().longValue());
        assertEquals(requestDto.getName(), expectedCategoryResponse.getName());
        assertEquals(requestDto.getDescription(), expectedCategoryResponse.getDescription());
    }

    @Test
    @DisplayName("Get All Categories: Empty List, Successful Request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllCategories_EmptyList_SuccessfulRequest() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(this.categoryService.findAll(pageable)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Get All Categories: Non-Empty List, Successful Request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllCategories_SuccessfulRequest_NonEmptyList() throws Exception {
        List<CategoryResponseDto> categoryList = new ArrayList<>();
        CategoryResponseDto category1 = new CategoryResponseDto();
        category1.setId(1L);
        category1.setName("Category 1");
        categoryList.add(category1);
        CategoryResponseDto category2 = new CategoryResponseDto();
        category2.setId(2L);
        category2.setName("Category 2");
        categoryList.add(category2);
        Mockito.when(categoryService.findAll(any(Pageable.class)))
                .thenReturn(categoryList);
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Create New Category: Successful Request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createNewCategory_SuccessfulRequest() throws Exception {
        CategoryRequestDto categoryRequest = new CategoryRequestDto();
        categoryRequest.setName("New Category");
        CategoryResponseDto newCategory = new CategoryResponseDto();
        newCategory.setId(1L);
        newCategory.setName("New Category");
        Mockito.when(categoryService.save(categoryRequest)).thenReturn(newCategory);
        String content = (new ObjectMapper()).writeValueAsString(categoryRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(requestBuilder);
    }

    @Test
    @DisplayName("Test Delete Category by ID: Successful Request")
    public void deleteCategoryById_SuccessfulRequest() throws Exception {
        doNothing().when(this.categoryService).deleteById(any());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/categories/4");
        MockMvcBuilders.standaloneSetup(this.categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test Get Category by ID: Successful Request")
    public void getCategoryById_SuccessfulRequest() throws Exception {
        Long categoryId = 1L;
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(categoryId);
        categoryDto.setName("Test Category");
        Mockito.when(categoryService.getById(categoryId)).thenReturn(categoryDto);
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.get("/categories/{id}", 1L);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Category"));
    }

    @Test
    @DisplayName("Test Get Books by Category ID: Successful Request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getBookByCategoryId_successfulRetrieval() throws Exception {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        BookWithoutCategoryIdsDto book1 = new BookWithoutCategoryIdsDto(
                1L,
                "Book 1",
                "Author 1",
                "ISBN 1",
                BigDecimal.valueOf(10.0),
                "Description 1",
                "coverImage1");
        List<BookWithoutCategoryIdsDto> fakeBooks = List.of(book1);
        when(bookService.findAllByCategoryId(eq(categoryId), eq(pageable))).thenReturn(fakeBooks);
        mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(fakeBooks)));
    }

    @Test
    @DisplayName("Test Update Category: Successful Update Request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateCategory_SuccessfulRequest() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Updated Category");
        categoryRequestDto.setDescription("Updated description");
        CategoryResponseDto updatedCategoryResponse = new CategoryResponseDto();
        updatedCategoryResponse.setId(categoryId);
        updatedCategoryResponse.setName(categoryRequestDto.getName());
        updatedCategoryResponse.setDescription(categoryRequestDto.getDescription());
        Mockito.when(categoryService.update(Mockito.eq(categoryId),
                        Mockito.any(CategoryRequestDto.class)))
                .thenReturn(updatedCategoryResponse);
        String content = new ObjectMapper().writeValueAsString(categoryRequestDto);
        MockHttpServletRequestBuilder requestBuilder
                = MockMvcRequestBuilders.put("/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"id\":1,\"name\":\"Updated Category\","
                                + "\"description\":\"Updated description\"}"));
    }

    public static String convertObjectToJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
