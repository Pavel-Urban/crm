/*globals it, describe, beforeEach, browser, expect, beforeAll, afterAll, require*/
'use strict';

describe('Users under role ADMIN', function () {
  var loginService = require('../../login.service.js');
  var usersPage = require('../../pages/users.po.js');
  var credentials = require('../../credentials');
  var usersFormPO = require('../../pages/users.form.po.js');
  var contactsPage = require('../../pages/contacts.po.js');
  var navbarPO = require('../../pages/navbar.po');

  var ROLE_BLOCKING_USER = 'MANAGER';
  var ROLE_FILTERING_USER = 'ADMIN';
  var ROLE_CHANGING_ROLE_USER = 'MANAGER';
  var ADMIN = credentials.admin;
  var BLOCKING_USER = credentials.manager;
  var FILTERING_USER = credentials.admin;
  var CHANGING_ROLE_USER = credentials.admin;

  beforeEach(function () {
    loginService.login(ADMIN)
  });

  it('should be able to filter user records by roles', function () {
    usersPage.getOption(ROLE_FILTERING_USER).click();
    iterateThroughPages(isFilteringUserOnPage)
      .then(expectFoundToBeTrue);
  });

  it('should be able to block the user and check if it`s blocked', function () {
    var userToBlock = BLOCKING_USER.username;
    usersPage.searchTab.sendKeys(userToBlock);
    usersPage.getOption(ROLE_BLOCKING_USER).click();
    iterateThroughPages(isBlockingUserOnPage).then(expectFoundToBeTrue);
    activateUser(false, userToBlock);
    expect(checkUserOnPage(userToBlock)).toBe(false);
    usersPage.activeUserFlag.click();
    expect(checkUserOnPage(userToBlock)).toBe(true);
    activateUser(true, userToBlock);
    usersPage.activeUserFlag.click();
    expect(checkUserOnPage(userToBlock)).toBe(true);
  });

  it('should be able to add Manager role to admin and access contacts page', function () {
    var userChangingRole = CHANGING_ROLE_USER.username;
    iterateThroughPages(isChangingRoleUserOnPage).then(expectFoundToBeTrue);
    usersPage.editUserClick(userChangingRole);
    usersFormPO.checkRole(ROLE_CHANGING_ROLE_USER);
    usersFormPO.submitButton.click();
    loginService.logout();
    loginService.login(CHANGING_ROLE_USER);
    navbarPO.contactsLink.click();
    expect(contactsPage.searchTab.isPresent()).toBe(true);
    loginService.logout();
    loginService.login(ADMIN);
    iterateThroughPages(isChangingRoleUserOnPage).then(expectFoundToBeTrue);
    usersPage.editUserClick(userChangingRole);
    usersFormPO.uncheckRole(ROLE_CHANGING_ROLE_USER);
    usersFormPO.submitButton.click();
  });

  afterEach(function () {
    loginService.logout();
  });

  function expectFoundToBeTrue(found) {
    expect(found).toBe(true);
  }

  function iterateThroughPages(checkingFunction) {
    return checkingFunction()
      .then(function (isFound) {
        return checkingLoop(isFound, checkingFunction);
      }).then(function (elementInPage) {
        return elementInPage.found;
      });
  }

  function isFilteringUserOnPage(isLastPage) {
    return checkUserOnPage(FILTERING_USER.username)
      .then(function (found) {
        return ElementInPage(found, isLastPage);
      })
  }

  function isBlockingUserOnPage(isLastPage) {
    return checkUserOnPage(BLOCKING_USER.username)
      .then(function (found) {
        return ElementInPage(found, isLastPage);
      })
  }

  function isChangingRoleUserOnPage(isLastPage) {
    return checkUserOnPage(CHANGING_ROLE_USER.username)
      .then(function (found) {
        return ElementInPage(found, isLastPage);
      })
  }

  function activateUser(isMakeActive, userName) {
    usersPage.clickOnCheckboxOfUser(userName);
    if (isMakeActive) {
      usersPage.userActivation.click();
    } else {
      usersPage.userDeactivation.click();
    }
  }

  function checkingLoop(initElOnPage, checkingFunction) {
    return goToNextPageIfNotLast(initElOnPage.found)
      .then(function (elOnPage) {
        if (elOnPage.found) {
          return elOnPage;
        } else {
          return checkingFunction(elOnPage.lastPage)
            .then(function (elementOnPage) {
              return loopIfPageNotLast(elementOnPage, checkingFunction);
            });
        }
      });
  }

  function loopIfPageNotLast(elementOnPage, checkingFunction) {
    if (elementOnPage.lastPage) {
      return elementOnPage;
    } else {
      return checkingLoop(elementOnPage, checkingFunction);
    }
  }

  function goToNextPageIfNotLast(elementFound) {
    return usersPage.isLastPage()
      .then(function (isLastPage) {
          if (isLastPage || elementFound) {
            return ElementInPage(elementFound, isLastPage);
          } else {
            return usersPage.nextPageButton().click()
              .then(function () {
                return ElementInPage(elementFound, isLastPage);
              });
          }
        }
      );
  }

  function ElementInPage(elementFound, isLastPage) {
    return {found: elementFound, lastPage: isLastPage}
  }

  function checkUserOnPage(userName) {
    return usersPage.getUserNamesInTable().then(function (users) {
      return checkIfUserInArray(users, userName);
    })
  }

  function checkIfUserInArray(users, name) {
    return (users.indexOf(name)) != -1;
  }
});
