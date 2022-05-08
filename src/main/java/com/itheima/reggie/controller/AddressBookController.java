package com.itheima.reggie.controller;

import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Resource
    private AddressBookService addressBookService;

    /**
     * 地址列表
     * @return
     */
    @GetMapping("/list")
    public CommonResult<List<AddressBook>> list(){
        return addressBookService.listAddressBooks();
    }

    /**
     * 回显地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public CommonResult<AddressBook> queryAddressById(@PathVariable("id") Long id){
        return addressBookService.queryAddressById(id);
    }

    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public CommonResult<String> removeAddress(Long ids){
        return addressBookService.removeAddress(ids);
    }


    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public CommonResult<AddressBook> defaultAddress(@RequestBody AddressBook addressBook){
        return addressBookService.defaultAddress(addressBook);
    }

    @GetMapping("default")
    public CommonResult<AddressBook> getDefault(){
        return addressBookService.getDefault();
    }

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public CommonResult<String> saveAddress(@RequestBody AddressBook addressBook){
        return addressBookService.saveAddress(addressBook);
    }

    @PutMapping
    public CommonResult<String> updateAddress(@RequestBody AddressBook addressBook){
        return addressBookService.updateAddress(addressBook);
    }
}
