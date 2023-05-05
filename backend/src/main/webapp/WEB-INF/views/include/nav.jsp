<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	<div class="container px-5">
		<a class="navbar-brand" href="${root}/index">Where Is My House</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav ms-auto mb-2 mb-lg-0">
				<li class="nav-item"><a class="nav-link" href="${root}/index">Home</a></li>
				<!-- <li class="nav-item"><a class="nav-link" href="about.html">About</a></li> -->
				<c:choose>
					<c:when test='${empty userInfo}'>
						<li class="nav-item"><a class="nav-link" href="${root}/login">로그인</a></li>
						<li class="nav-item"><a class="nav-link" href="${root}/signup">회원가입</a></li>
					</c:when>
					<c:otherwise>
						<li class="nav-item"><a class="nav-link" href="${root}/house/favorite?id=${userInfo.id}">관심지역</a></li>
						<li class="nav-item"><a class="nav-link" href="${root}/userinfo?id=${userInfo.id}">마이 페이지</a></li>
						<li class="nav-item"><a class="nav-link" href="${root}/logout">로그아웃</a></li>
						<li class="nav-item"><a class="nav-link" href="${root}/qna">QnA</a></li>
						<!-- 검색 토글 -->
						<li class="nav-item dropdown">
							<a 
							class="nav-link dropdown-toggle" 
							id="navbarDropdownBlog" 
							href="#" role="button" 
							data-bs-toggle="dropdown" 
							aria-expanded="false"
							>실거래가 조회</a
							>
							<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownBlog">
								<li><a class="dropdown-item" href="${root}/house/dongFindForm">동별 조회</a></li>
								<li><a class="dropdown-item" href="${root}/house/aptFindForm">아파트별 조회</a></li>
							</ul>
						</li>
					</c:otherwise>
				</c:choose>
				

			</ul>
		</div>
	</div>
</nav>