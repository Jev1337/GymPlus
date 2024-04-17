(function($) {
    /* "use strict" */
    function onScanSuccess(decodedText, decodedResult) {
        // Handle on success condition with the decoded text or result.
        console.log(`Scan result: ${decodedText}`, decodedResult);
        //send ajax request to /api/getUserSubDetails/${decodedText}
        $.ajax({
            url: `/api/getUserSubDetails/${decodedText}`,
            type: 'GET',
            success: function(data) {
                // result sample: {"status":true,"subtype":"GP 2","expdate":{"date":"2024-10-08 00:00:00.000000","timezone_type":3,"timezone":"Europe\/Berlin"},"userid":12341443,"userfn":"AWDWD","userln":"Amir","userdob":{"date":"2024-03-13 00:00:00.000000","timezone_type":3,"timezone":"Europe\/Berlin"}}
                console.log(data);
                document.getElementById("profilepic").src = "/profileuploads/"+data.userphoto;
                document.getElementById("fullname").innerHTML = data.userfn + " " + data.userln;
                document.getElementById("dob").innerHTML = data.userdob.date;

                document.getElementById("cin").innerHTML = data.userid;
                if(data.status == true){
                    document.getElementById("icon").className = "fa fa-check-circle";
                    document.getElementById("icon").style.color = "green";
                    document.getElementById("subType").innerHTML = data.subtype;
                    document.getElementById("subDate").innerHTML = data.expdate.date;
                }else
                {
                    document.getElementById("icon").className = "fa fa-times-circle";
                    document.getElementById("icon").style.color = "red";
                    document.getElementById("subType").innerHTML = "N/A";
                    document.getElementById("subDate").innerHTML = "N/A";
                }
            }
        });
    }
    
    var html5QrcodeScanner = new Html5QrcodeScanner(
        "reader", { fps: 10, qrbox: 250 });
    html5QrcodeScanner.render(onScanSuccess);
    console.log("html5QrcodeScanner.render(onScanSuccess);");
})(jQuery);