<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Singup -->
<section class="container g-py-100">
    <div class="row justify-content-center">
        <div class="col-sm-10 col-md-9 col-lg-6">
            <div class="g-brd-around g-brd-gray-light-v4 rounded g-py-40 g-px-30">

                Registrierung abgeschlossen. Bitte aktivieren das Konto �ber den
                Link in der Mail, die wir gerade an ${user.email} geschickt haben.
                
                Debug Link: <a href="<c:url value="verify?verificationKey=${user.verificationKey}" />">Verifizierungslink</a></div>
            </div>
        </div>
    </div>
</section>
<!-- End Singup -->
