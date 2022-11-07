/***************** START sweetAlert JS *****************/
/*
 * @param
 * msg : 알림 메세지,
 * titie : 알림창 제목
 * btnText : 버튼이름
 * icon: 알림창 유형 (warning,error,success,info)
 */
const alert = (msg, title, btnText, icon) => {
    title =  empty(title) == '' ? '' : title;
    icon = empty(icon) == '' ? '' : type;
    btnText = empty(btnText) == '' ? '확인' : type;

    swal({
        title: title,
        text: msg,
        icon: icon,
        button: btnText,
    });
}

/*
 * @param
 * msg : 알림 메세지,
 * titie : 알림창 제목
 * icon: 알림창 유형 (warning,error,success,info)
 */
// TODO 에러발생 확인 필요 2022.11.02
const confirm = (msg, callbackMsg, title, BtnConfirmMsg, BtnCancelMsg, icon) => {
    title =  empty(title) == '' ? '' : title;
    callbackMsg =  empty(callbackMsg) == '' ? '' : BtnCancelMsg;
    BtnConfirmMsg = empty(BtnConfirmMsg) == '' ? '' : BtnConfirmMsg;
    BtnCancelMsg =  empty(BtnCancelMsg) == '' ? '' : BtnCancelMsg;
    icon = empty(icon) == '' ? 'warning' : type;

    swal({
        title: title,
        text: msg,
        icon: icon,
        buttons: true,
        dangerMode: true
//        confirmButtonText : "예",
//        BtnCancelMsg : "아니요",
    })
    .then((willDelete) => {
        if (willDelete) {
            swal(callbackMsg, {
            icon: "success",
            });
        }
    });
}
/***************** END sweetAlert JS *****************/


/*
 * @param : String
 * @설명 : 공백확인
 */
function empty(str){
    if(typeof str == "undefined" || str == null || str == "") { return ''; }
    else{ return str ;}
}


/***************** END Ajax JS *****************/

// JAVASCRIPT AJAX UTIL - ENV
var ENV_AJAX = {
    methodType: {GET: "GET", POST: "POST"},
    header: {
        JSON: {"Content-Type": "application/json; charset=UTF-8"},
        HTML: {"Content-Type": "text/html; charset=UTF-8"},
    },
    reqContentsType: {
        JSON: "application/json; charset=utf-8",
        HTML: "text/html; charset=UTF-8",
        FILE: "multipart/form-data",
    },
    FILE: {
        MAX_SIZE: "10485760",
        NAME_MAX_BYTE: "499",
    },
    resType: {JSON: "JSON", HTML: "HTML", XML: "XML"},
//  orgin: String(location.protocol).concat('//', location.host),
};


var NetUtils = {
    request_RestApiView: function(req_url, type, res_callback){
        let error_exist = false;

         $.ajax({
            url: req_url,
            type: type,
        })
        .done(function(rsData) {
            console.log('done start');
        })
        .fail(function(a,b,c){
            console.log('fail start');
            error_exist = true;
        })
        .always(function(rsData,b,c){
            console.log('awlays start');
            if(empty(res_callback) != '' && typeof res_callback === 'function'){
                if(error_exist === false){
                    res_callback(rsData);
                }
            }else{
                if(error_exist === false){
                    result = rsData;
                }
            }
        })
        .then(function(a,b,c){
            console.log('then start');
        });
    },

    requester_async: function(req_url, send_data, type, content_type, res_type, res_callback){
        let result;
        let error_exist = false;

        $.ajax({
            url : req_url,
            data : send_data,
            type : type,                // GET, POST
            async : "true",
            contentType : content_type, // 서버에 데이터를 보낼떄 데이터 유형
            dataType : res_type,        // 서버에서 받는 데이터 유형
            beforeSend: function(){
                // XHR Header 포함, HTTP Request 하기전에 호출
                console.log('ajax beforeSend start!');
            },
        })
        .done(function(rsData) {
            console.log('done start');
            console.log(rsData);
        })
        .fail(function(rsData,b,c){
            console.log('fail start');
            error_exist = true;
        })
        .always(function(rsData,b,c){
            console.log('awlays start');
            if(empty(res_callback) != '' && typeof res_callback === 'function'){
                if(error_exist === false){
                    res_callback(rsData.responseText);
                }
            }else{
                if(error_exist === false){
                    result = rsData.responseText;
                }
            }
        })
        .then(function(rsData,b,c,d){
            console.log('then start');
        });

        return result;
    },

    getPostData: function(req_url, send_data, res_callback){
        let rs;
        res_callback = empty(res_callback);

        if(res_callback != '' && typeof res_callback === "function"){
            return this.requester_async(req_url, send_data, ENV_AJAX.methodType.POST, ENV_AJAX.reqContentsType.JSON, ENV_AJAX.resType.JSON, res_callback);
        }else{
            rs = this.requester_async(req_url, send_data, ENV_AJAX.methodType.POST, ENV_AJAX.reqContentsType.JSON, ENV_AJAX.resType.JSON, null);
        }

        return rs;
    },

    getRestAPIView: function(req_url, type, res_callback){
        let rs;
        res_callback = empty(res_callback);

        if(res_callback != '' && typeof res_callback === "function"){
            return this.request_RestApiView(req_url, type, res_callback);
        }

        return rs;
    }
}

/***************** Start DataManager JS *****************/

// JS.DATA UTIL - TRANSFER OBJECT
var DataManager = {
    // private
    shared_data: new Map(),
    instance: null,
    data_list: 0,
    // public
    initData: function (popup_name) {
        var key = isEmpty(popup_name) ? "g_idx_" + this.data_list : popup_name;
        localStorage.removeItem(key);
        localStorage.setItem(key, null);
        this.data_list++;
        return key;
    },
    setData: function (key, val) {
        if (typeof key !== 'string') {
            console.log("[setData] arg key is not string type..");
            return false;
        }
        if (typeof val !== 'object') {
            console.log("[setData] arg value is not invalid object type..");
            return false;
        }
        try {
            localStorage.setItem(key, TypeUtil.toJsonStr(val));
            return true;
        } catch (e) {
            console.log(e);
            return false;
        }
    },
    data_drain: function (key) {
        try {
            localStorage.setItem(key, '');
            localStorage.removeItem(key);
            return true;
        } catch (e) {
            console.log(e);
            return false;
        }
    },
    getData: function (key) {
        if (typeof key !== 'string') {
            console.log("[getData] arg key is not string type..");
            return false;
        }
        try {
            if (String(key).length > 3) {
                // return JSON.parse(search_data[key]);
                return TypeUtil.toJsonObj(localStorage.getItem(key));
            } else {
                console.log("{DS.CALL::DATA_NULL -> ", key, "}");
            }
        } catch (e) {
            console.log(e);
            return false;
        }
    },
    setBigData: function (key, req_obj) {
        if (typeof key !== 'string') {
            console.log("[setBigData] arg key is not string type..");
            return false;
        }
        if (typeof req_obj !== 'object') {
            console.log("[setBigData] arg value is not invalid object type..");
            return false;
        }
        try {
            var requestObject = initBigDataAdd(key, req_obj);
            // console.log(requestObject, ENV_AJAX.orgin);
            console.log("getMenu:rpc execute..");
            // window.parent.postMessage(menuObject, ENV_AJAX.orgin);
            parent.postMessage(requestObject, ENV_AJAX.orgin);
        } catch (e) {
            console.log(e);
            return false;
        }
    },
    getBigData: function (key) {
        if (typeof key !== 'string') {
            console.log("[getData] arg key is not string type..");
            return false;
        }
        try {
            if (String(key).length > 3) {
                return parent.storage_data_getter(key, ENV_AJAX.orgin);
            } else {
                console.log("{DS.CALL::DATA_NULL -> ", key, "}");
            }
        } catch (e) {
            console.log(e);
            return false;
        }
    },
};
/***************** End DataManager JS *****************/

/***************** Start TypeUtil JS *****************/
// JS TYPE FILTER, CASTER
var TypeUtil = {
    toJsonStr: function (val_obj) {
        try {
            if (val_obj === null) {return null;}
            if (val_obj === undefined) {return null;}
            if (val_obj === '') {return null;}
            if (typeof val_obj == "boolean") {return null;}
            if (CheckUtil.isEmptyObject(val_obj)) {return null;}
            if (typeof val_obj === 'string') {return val_obj;}
            if (typeof val_obj === 'object') {
                try {
                    return JSON.stringify(val_obj);
                } catch (e) {
                    return null;
                }
            }
        } catch (e) {
            console.log(e);
        }
    },
    toJsonObj: function (val_str) {
        try {
            if (val_str === null) {return null;}
            if (val_str === undefined) {return null;}
            if (val_str === '') {return null;}
            if (typeof val_str === 'boolean') {return null;}
            if (CheckUtil.whatIsIt(val_str) === "Array") {return val_str;}
            if (typeof value === 'object') {return null;}
            if (typeof val_str === 'string') {
                try {
                    if (typeof JSON.parse(val_str) === 'object') {
                        return JSON.parse(val_str);
                    } else {
                        return null;
                    }
                } catch (e) {
                    return null;
                }
            }
            if (typeof val_str === 'object') {return val_str;}
        } catch (e) {
            console.log(e);
            return null;
        }
    },
    isString: function (req_obj){
        if (this.whatIsIt(req_obj) === 'String') {return true;}
    },
    /** Form Data 직렬화 */
    getFormSerialize: function (formId) {
        var form = this.isString(formId) ? "form1" : formId;
        var str = "";
        str = $("#" + form).formSerialize();
        str = decodeURIComponent((str + '').replace(/\+/g, '%20'));
        return str;
    },
    /** Data 직렬화2 */
    getObjectSerialize: function (objId) {
        objId = this.isString(objId) ? objId : null;
        var str = "";
        str = $("#" + objId).find("input, select, textarea").serialize();
        str = decodeURIComponent((str + '').replace(/\+/g, '%20'));
        return str;
    },
    /** 마우스 X좌표 */
    mouseCoordinateX: function (e) {
        var mouse_x = 0;

        // 이벤트 검사 --> 이벤트시 onClick를 사용해야함. href경우 사용 안됨.
        if (!e) var e = window.event;
        if (e.pageX) {					// pageX/Y 표준 검사
            mouse_x = e.pageX;
        } else if (e.clientX) {			// clientX/Y 표준 검사 Opera
            mouse_x = e.clientX;
            if (navigator.appName.indexOf("Internet Explorer") >= 0) { // IE 여부 검사
                mouse_x += document.body.scrollLeft;
            }
        }
        return mouse_x;
    },
    /** 마우스 Y좌표 */
    mouseCoordinateY: function (e) {
        var mouse_y = 0;

        if (!e) var e = window.event;
        if (e.pageY) {
            mouse_y = e.pageY;
        } else if (e.clientY) {
            mouse_y = e.clientY;
            if (navigator.appName.indexOf("Internet Explorer") >= 0) {
                mouse_y += document.body.scrollTop;
            }
        }
        return mouse_y;
    },
    /**
     * @param container drag 적용할 div class
     * @param excpTd drag 미적용할 th 또는 td class
     */
    fn_dragContainer: function (container, excpCol) {
        var dragFlag = false;
        var pre_x;
        var x;
        $('.' + container).hover(function () {
            $(this).css('cursor', 'pointer');
        });
        $('.' + container + ' th:not(.' + excpCol + '),' + '.' + container + ' td:not(.' + excpCol + ')').mousedown(
            function (e) {
                dragFlag = true;
                var obj = $('.' + container);
                x = obj.scrollLeft();
                pre_x = e.screenX;
                $(this).css("cursor", "pointer");
            }
        );
        $('.' + container + ' th:not(.' + excpCol + '),' + '.' + container + ' td:not(.' + excpCol + ')').mousemove(
            function (e) {
                if (dragFlag) {
                    var obj = $('.' + container);
                    obj.scrollLeft(x - e.screenX + pre_x);
                    return false;
                }
            }
        );
        $('.' + container).mouseup(
            function () {
                dragFlag = false;
            }
        );
    },
};
/***************** End TypeUtil JS *****************/


/***************** Start CheckUtil JS *****************/
var CheckUtil = {
    // JS object type checker
    stringConstructor: "test".constructor,
    arrayConstructor: [].constructor,
    objectConstructor: ({}).constructor,
    whatIsIt: function (req_obj) {
        if (req_obj === null) {return "null";}
        if (req_obj === undefined) {return "undefined";}
        if (req_obj.constructor === this.stringConstructor) {return "String";}
        if (req_obj.constructor === this.arrayConstructor) {return "Array";}
        if (req_obj.constructor === this.objectConstructor) {return "Object";}
        {return "un-known type";}
    },
    // 함수 타잎 확인
    isFunction: function (func) {
        return (func !== null) && ((typeof func) === String("function"));
    },
    // JSON String check
    isJsonString: function (str) {
        try {
            var json = JSON.parse(str);
            //return (typeof json === 'object');
            // TODO : return object or array
            return (json.constructor === Object || json.constructor === Array);
        } catch (e) {
            return false;
        }
    },
    ignition: function (arg) {
        return arg !== undefined && typeof arg !== "undefined";
    },
    // 객체 선언, NULL 확인
    exist: function (val) {
        try {
            if (typeof val === 'undefined') return false;
            if (val === undefined) return false;
            return val !== null;
        } catch (e) {
            return false;
        }
    },
    // HTTP STATUS OK
    requestComplete: function (xhr_ready_state, xhr_status_num) {
        var ready_state = (xhr_ready_state === Number(4));
        var status = (xhr_status_num === Number(200));

        return (status && ready_state);
    },
    // 빈 객체 탐색
    isEmptyObject: function (obj) {
        if (obj === null) {return true;}
        if (obj === undefined) {return true;}
        if (obj === '') {return true;}
        if (typeof obj !== 'object') {return true;}

        return (Object.keys(obj).length <= 0);
    },
    // 빈(size 0) 배열 탐색
    isEmptyArray: function (obj) {
        if (obj === null) {return true;}
        if (obj === undefined) {return true;}
        if (typeof obj !== 'object') {return true;}
        if (!Array.isArray(obj)) {return true;}

        return (obj.length <= 0);
    },
    /** null 체크 */
    isString: function (value, init) {
        var initval = (init == undefined || init == null || init == "undefined") ? "" : init;
        return (value == undefined || value == null || value == "undefined") ? initval : value.toString();
    },
    /** 숫자 null 체크 */
    isNumber: function (value, init) {
        var initval = (init == undefined || init == null || init == "undefined") ? 0 : init;
        return (value == undefined || value == null || value == "undefined" || value == "") ? initval : value.replaceAll(",", "");
    },
};
/***************** End CheckUtil JS *****************/