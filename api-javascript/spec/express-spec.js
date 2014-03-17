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
    })


    it("providers is an array", function () {
        expect($.express.providers).isA(Array)
    })



})
