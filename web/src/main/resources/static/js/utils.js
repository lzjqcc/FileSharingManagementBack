function rgbToHexString(rgb) {
    var rgb = rgb.split(',');
    var r = parseInt(rgb[0].split('(')[1]);
    var g = parseInt(rgb[1]);
    var b = parseInt(rgb[2].split(')')[0]);
    return hex = "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);

}

function utf16to8(str) {
    var out, i, len, c;
    out = "";
    len = str.length;
    for (i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
            out += str.charAt(i);
        } else if (c > 0x07FF) {
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
            out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        } else {
            out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
            out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        }
    }
    return out;
}
function utf82str(utf)
{
    var str = "";
    var tmp;

    for(var i = 0; i < utf.length; i++)
    {
        // 英文字符集合
        if(utf.charCodeAt(i) >> 7 == 0x00)
        {
            str += utf.charAt(i);
            continue;
        }
        // 其他字符集
        else if(utf.charCodeAt(i) >> 5 == 0x06)
        {
            tmp = ((utf.charCodeAt(i + 0) & 0x1f) << 6) |
                ((utf.charCodeAt(i + 1) & 0x3f) << 0);
            str += String.fromCharCode(tmp);
            i++;
            continue;
        }
        // 中文字符集
        else if(utf.charCodeAt(i) >> 4 == 0x0e)
        {
            tmp = ((utf.charCodeAt(i + 0) & 0x0f) << 12) |
                ((utf.charCodeAt(i + 1) & 0x3f) <<  6) |
                ((utf.charCodeAt(i + 2) & 0x3f) <<  0);
            str += String.fromCharCode(tmp);
            i += 2;
            continue;
        }
        // 其他字符集
        else if(utf.charCodeAt(i) >> 3 == 0x1f)
        {
            tmp = ((utf.charCodeAt(i + 0) & 0x07) << 18) |
                ((utf.charCodeAt(i + 1) & 0x3f) << 12) |
                ((utf.charCodeAt(i + 2) & 0x3f) <<  6);
            ((utf.charCodeAt(i + 3) & 0x3f) <<  0);
            str += String.fromCharCode(tmp);
            i += 3;
            continue;
        }
        str += utf[i];
    }

    return str;
}

//下载
function downloadFile(imageName, base64Image) {
    var aLink = document.createElement('a');
    var blob = this.base64ToBlob(base64Image); //new Blob([content]);
    var evt = document.createEvent("HTMLEvents");
    evt.initEvent("click", true, true);//initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
    aLink.download = imageName;
    aLink.href = URL.createObjectURL(blob);
    // aLink.dispatchEvent(evt);
    aLink.click()
}
//base64转blob
function base64ToBlob(code) {
    var parts = code.split(';base64,');
    var contentType = parts[0].split(':')[1];
    var raw = window.atob(parts[1]);
    var rawLength = raw.length;

    var uInt8Array = new Uint8Array(rawLength);

    for (var i = 0; i < rawLength; ++i) {
        uInt8Array[i] = raw.charCodeAt(i);
    }
    return new Blob([uInt8Array], {type: contentType});
}

function createQrCode(content) {

}