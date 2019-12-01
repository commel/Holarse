<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

Available Users:
<ul>
    <c:forEach items="${users}" var="user">
        <li><a href="${user.url}">${user.login}</a></li>
    </c:forEach>
</ul>

