<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- Form -->
<form:form modelAttribute="articleCommand" action="/wiki/" method="POST">
    <form:label path="title">Titel</form:label>
    <form:input path="title" class="form-control"></form:input>
    <form:label path="tags">Tags</form:label>
    <form:input path="tags" class="form-control"></form:input>    
    <form:label path="content">Inhalt</form:label>
    <form:textarea path="content" class="form-control"></form:textarea>
    <form:button class="form-control">Anlegen</form:button>
</form:form>
<!-- End Form -->

