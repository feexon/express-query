/**
 * @author Administrator
 * @version 1.0 14-3-14,下午4:11
 */

describe("express", function () {

    describe("query", function () {

        it("options", function () {
            //todo:: test known how to do query ,expose internals
            var callback = function () {
            };
            var options = {};
            $.ajax = function (url, settings) {
                $.extend(options, {url: url}, settings);
            };

            $.express.query("yunda", "1201088260402", callback);

            expect(options.url).toEqual($.express.defaults.url);
            expect(options.data.type).toEqual("yunda");
            expect(options.data.postid).toEqual("1201088260402");
            expect(options.compelte).toBe(callback);

        })

        it("unknown post type", function () {
            expect(function () {
                $.express.query("unknown", "1201088260402", null);
            }).toThrow("Unknown provider:unknown")
        })
    });


    it("providers is an array", function () {
        expect($.express.providers).isA(Array);
    });


    var records = '{"message":"ok","nu":"1201088260402","ischeck":"1","com":"yunda","updatetime":"2014-03-17 10:29:40","status":"200","condition":"F00","data":[{"time":"2014-01-14 20:43:19","context":"广东广州番禺区公司市桥光明分部:快件已被 拍照 签收","ftime":"2014-01-14 20:43:19"},{"time":"2014-01-14 20:20:36","context":"广东广州番禺区公司市桥光明分部:进行快件扫描，将发往：广东广州番禺区公司市桥光明分部","ftime":"2014-01-14 20:20:36"},{"time":"2014-01-13 10:22:45","context":"广东广州番禺区公司市桥光明分部:到达目的地网点，快件将很快进行派送","ftime":"2014-01-13 10:22:45"},{"time":"2014-01-13 09:33:31","context":"广东广州番禺区公司:进行快件扫描，将发往：广东广州番禺区公司市桥光明分部","ftime":"2014-01-13 09:33:31"},{"time":"2014-01-13 01:44:14","context":"广东广州分拨中心:从站点发出，本次转运目的地：广东广州番禺区公司","ftime":"2014-01-13 01:44:14"},{"time":"2014-01-13 01:38:03","context":"广东广州分拨中心:快件进入分拨中心进行分拨","ftime":"2014-01-13 01:38:03"},{"time":"2014-01-12 01:10:45","context":"上海分拨中心:进行装车扫描，即将发往：广东广州分拨中心","ftime":"2014-01-12 01:10:45"},{"time":"2014-01-12 01:10:03","context":"上海分拨中心:在分拨中心进行卸车扫描","ftime":"2014-01-12 01:10:03"},{"time":"2014-01-11 21:40:50","context":"上海金山分拨中心:进行装车扫描，即将发往：上海分拨中心","ftime":"2014-01-11 21:40:50"},{"time":"2014-01-11 20:57:48","context":"上海金山分拨中心:在分拨中心进行称重扫描","ftime":"2014-01-11 20:57:48"},{"time":"2014-01-11 19:00:21","context":"上海金山区松隐公司蒋峰淘宝服务部:进行下级地点扫描，将发往：广东广州网点包","ftime":"2014-01-11 19:00:21"},{"time":"2014-01-11 15:21:24","context":"上海金山区松隐公司蒋峰淘宝服务部:进行揽件扫描","ftime":"2014-01-11 15:21:24"}],"state":"3"}';
    it("print express", function () {
        $.express.query = function (type, postid, callback) {
            expect(type).toEqual('yunda');
            expect(postid).toEqual('1201088260402');
            expect(callback).isA(Function);
            callback.call(null, {success: true, responseText: records});
        }
        var testee = $("<div id='test' type='yunda' post_id='1201088260402'>");
        testee.print_express();

        expect($("div", testee).length).toBe(12);
        expect($("div", testee).last()).hasClass("active");
        expect($("div>time", testee).length).toBe(12);
        expect($("div>span", testee).length).toBe(12);
        expect($("div.active>time", testee).text()).toEqual("2014-01-14 20:43:19");
        expect($("div.active>span", testee).text()).toEqual("广东广州番禺区公司市桥光明分部:快件已被 拍照 签收");
    });



})
