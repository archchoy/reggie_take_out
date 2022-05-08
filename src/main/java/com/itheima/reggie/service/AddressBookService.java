package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    CommonResult<List<AddressBook>> listAddressBooks();

    CommonResult<AddressBook> queryAddressById(Long id);

    CommonResult<String> removeAddress(Long id);

    CommonResult<String> updateAddress(AddressBook addressBook);

    CommonResult<AddressBook> defaultAddress(AddressBook addressBook);

    CommonResult<String> saveAddress(AddressBook addressBook);

    CommonResult<AddressBook> getDefault();

}
